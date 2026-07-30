package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;
import com.nexus.rag.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.rag.application.mapper.ProductStockViewMapper;
import com.nexus.rag.domain.exception.SearchPipelineException;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import com.nexus.rag.domain.service.PromptSanitizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private static final double SIMILARITY_THRESHOLD = 0.5;
    private static final int TOP_K = 20;

    private final ChatClient chatClient;
    private final ProductStockViewRepository productStockViewRepository;
    private final ProductStockViewMapper productStockViewMapper;
    private final PromptSanitizer promptSanitizer;
    private final VectorStore vectorStore;
    private final PromptTemplate qwen3QueryInstructionTemplate;

    public ProductSearchServiceImpl(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            ProductStockViewRepository productStockViewRepository,
            ProductStockViewMapper productStockViewMapper,
            PromptSanitizer promptSanitizer,
            @Value("classpath:/prompts/rag-instructions.st") Resource ragInstructions,
            @Value("classpath:/prompts/rag-query-transformer-instructions.st") Resource queryTransformerInstructions,
            @Value("classpath:/prompts/qwen3-query-instruction.st") Resource qwen3QueryInstruction
    ) {

        this.productStockViewRepository = productStockViewRepository;
        this.productStockViewMapper = productStockViewMapper;
        this.promptSanitizer = promptSanitizer;
        this.vectorStore = vectorStore;
        this.qwen3QueryInstructionTemplate = new PromptTemplate(qwen3QueryInstruction);

        // VectorStore similarity search configuration
        DocumentRetriever retriever = this::retrieveDocuments;

        // Configuration for QueryTransformer (LLM call to rewrite/optimize the user query)
        QueryTransformer rewriteTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.build().mutate())
                .promptTemplate(new PromptTemplate(queryTransformerInstructions))
                .targetSearchSystem("VectorStore")
                .build();

        // Logging wrapper for QueryTransformer (Decorator)
        QueryTransformer rewriteTransformerWithLogging = query -> this.queryTransformerWithLogging(query, rewriteTransformer);

        // allowEmptyContext defaults to false: on no match, the model is instructed not to answer rather than the call being skipped
        QueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                .promptTemplate(new PromptTemplate(ragInstructions))
                .build();

        // Spring AI RAG Advisor wrapper configuration
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(rewriteTransformerWithLogging)
                .documentRetriever(retriever)
                .queryAugmenter(queryAugmenter)
                .build();

        this.chatClient = chatClientBuilder.defaultAdvisors(ragAdvisor).build();

    }

    @Override
    public ProductSearchResponse search(String userQuery) {

        try {

            log.info("[SEARCH PIPELINE] VectorStore implementation in use: {}", vectorStore.getClass().getSimpleName());
            log.info("[SEARCH PIPELINE] User query: \"{}\"", userQuery);

            String sanitizedQuery = promptSanitizer.sanitizeQuery(userQuery);
            log.info("[SEARCH PIPELINE] Sanitized query: \"{}\"", sanitizedQuery);

            ChatResponse chatResponse = chatClient.prompt(sanitizedQuery).call().chatResponse();
            if (chatResponse == null || chatResponse.getResult() == null) {
                throw new SearchPipelineException("Chat client failed");
            }

            // Extract answer from LLM response
            String answer = chatResponse.getResult().getOutput().getText();
            if (answer == null) {
                answer = "No Answer";
            }

            Usage usage = chatResponse.getMetadata().getUsage();
            log.info("[SEARCH PIPELINE] promptTokens={}, completionTokens={}, totalTokens={}", usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
            log.info("[SEARCH PIPELINE] Received LLM answer - \"{}\"", answer);

            List<Document> retrievedDocuments = chatResponse.getMetadata().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);
            if (retrievedDocuments == null || retrievedDocuments.isEmpty()) {
                return new ProductSearchResponse(answer, null);
            }

            log.info("[SEARCH PIPELINE] Found {} matching vectors", retrievedDocuments.size());

            List<Long> productIds = retrievedDocuments.stream()
                    .map(doc -> {
                        if (doc.getScore() != null) {
                            log.info(doc.getScore().toString());
                        }
                        return Long.valueOf(doc.getMetadata().get("productId").toString());
                    })
                    .distinct()
                    .toList();

            List<ProductStockView> products = productStockViewRepository.findByProductIdIn(productIds);
            log.info("[SEARCH PIPELINE] Found {} products in database", products.size());

            List<ProductStockViewResponse> productDtoList = productStockViewMapper.toResponse(products);
            return new ProductSearchResponse(answer, productDtoList);

        } catch (SearchPipelineException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new SearchPipelineException(e, "Unknown failure");
        }

    }

    /**
     * Retrieves relevant documents from the vector store by formatting the query using the
     * Qwen3 instruction template and executing a similarity search.
     *
     * @param query the search query containing text to match against vector embeddings
     * @return a list of {@link Document} objects returned by the vector store matching the similarity threshold
     */
    List<Document> retrieveDocuments(Query query) {

        String instructedText = qwen3QueryInstructionTemplate.render(Map.of("query", query.text()));
        SearchRequest searchRequest = SearchRequest.builder()
                .query(instructedText)
                .similarityThreshold(SIMILARITY_THRESHOLD)
                .topK(TOP_K)
                .build();
        return vectorStore.similaritySearch(searchRequest);

    }

    /**
     * Transforms an incoming query using the provided {@link QueryTransformer} and logs the
     * raw input along with the semantically rewritten output for pipeline diagnostic purposes.
     *
     * @param query              the original query to be transformed
     * @param rewriteTransformer the transformer implementation used to perform query rewriting
     * @return the semantically transformed {@link Query}
     */
    Query queryTransformerWithLogging(Query query, QueryTransformer rewriteTransformer) {

        try {
            // Execute the first LLM call to rewrite the query
            Query transformedQuery = rewriteTransformer.transform(query);
            // Log the semantic conversion (.text() retrieves the modified string)
            log.info("[SEARCH PIPELINE] Raw Input: \"{}\" -> Semantically Rewritten: \"{}\"", query.text(), transformedQuery.text());
            return transformedQuery;
        } catch (Exception e) {
            log.error("[SEARCH PIPELINE] Failed to rewrite query: \"{}\"", query.text(), e);
            throw e;
        }

    }

}

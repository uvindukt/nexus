package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;
import com.nexus.rag.application.dto.web.response.v1.ProductStockViewResponse;
import com.nexus.rag.application.mapper.ProductStockViewMapper;
import com.nexus.rag.domain.exception.SearchPipelineException;
import com.nexus.rag.domain.model.ProductStockView;
import com.nexus.rag.domain.repository.ProductStockViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ChatClient chatClient;
    private final ProductStockViewRepository productStockViewRepository;
    private final ProductStockViewMapper productStockViewMapper;

    public ProductSearchServiceImpl(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            ProductStockViewRepository productStockViewRepository,
            ProductStockViewMapper productStockViewMapper,
            @Value("classpath:/prompts/rag-with-context.st") Resource contextResource
    ) {

        this.productStockViewRepository = productStockViewRepository;
        this.productStockViewMapper = productStockViewMapper;

        // VectorStore similarity search configuration
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5) // Similarity metric threshold (eg: cosine similarity) between 0 - 1, 1 being exactly same
                .topK(1) // No. of similarity results (most similar)
                .build();

        // Configuration for QueryTransformer (LLM call to rewrite/optimize the user query)
        QueryTransformer rewriteTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.build().mutate())
                .build();

        // allowEmptyContext defaults to false: on no match, the model is instructed
        // not to answer rather than the call being skipped
        QueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                .promptTemplate(new PromptTemplate(contextResource))
                .build();

        // Spring AI RAG Advisor wrapper configuration
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(rewriteTransformer)
                .documentRetriever(retriever)
                .queryAugmenter(queryAugmenter)
                .build();

        this.chatClient = chatClientBuilder.defaultAdvisors(ragAdvisor).build();

    }

    @Override
    public ProductSearchResponse search(String userQuery) {

        try {

            log.info("User query received: {}", userQuery);

            ChatResponse chatResponse = chatClient.prompt(userQuery).call().chatResponse();
            if (chatResponse == null || chatResponse.getResult() == null) {
                throw new SearchPipelineException("Chat client failed");
            }

            String answer = chatResponse.getResult().getOutput().getText();
            if (answer == null) {
                answer = "No Answer";
            }

            log.info("Received LLM answer - {}", answer);

            List<Document> retrievedDocuments = chatResponse.getMetadata().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);
            if (retrievedDocuments == null || retrievedDocuments.isEmpty()) {
                return new ProductSearchResponse(answer, null);
            }

            log.info("Received Products");

            List<Long> productIds = retrievedDocuments.stream()
                    .map(doc -> Long.valueOf(doc.getMetadata().get("productId").toString()))
                    .distinct()
                    .toList();

            List<ProductStockView> products = productStockViewRepository.findByProductIdIn(productIds);
            List<ProductStockViewResponse> productDtoList = productStockViewMapper.toResponse(products);

            return new ProductSearchResponse(answer, productDtoList);

        } catch (SearchPipelineException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new SearchPipelineException(e, "Unknown failure");
        }

    }

}

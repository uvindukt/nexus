package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Value("classpath:/prompts/rag-no-context.st")
    private Resource emptyContextResource;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ProductSearchServiceImpl(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {

        this.vectorStore = vectorStore;

        // VectorStore similarity search configuration
        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.75) // Similarity metric threshold (eg: cosine similarity) between 0 - 1, 1 being exactly same
                .topK(5) // No. of similarity results (most similar)
                .build();

        // Configuration for QueryTransformer (LLM call to rewrite/optimize the user query)
        QueryTransformer rewriteTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.build().mutate())
                .build();

        // Spring AI RAG Advisor wrapper configuration
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(rewriteTransformer)
                .documentRetriever(retriever)
                .build();

        this.chatClient = chatClientBuilder.defaultAdvisors(ragAdvisor).build();

    }

    @Override
    public ProductSearchResponse search(String userQuery) {
        return null;
    }

}

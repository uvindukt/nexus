package com.nexus.rag.application.service;

import com.nexus.rag.application.dto.web.response.v1.ProductSearchResponse;

public interface ProductSearchService {

    ProductSearchResponse search(String userQuery);

}

package com.nexus.rag.domain.service;

import com.nexus.rag.domain.model.ProductStockView;

public interface ProductHashCalculator {

    /**
     * Computes an SHA-256 hash based on the product's descriptive fields.
     *
     * @param view The product stock view to hash.
     * @return A hex-encoded string of the SHA-256 hash.
     */
    String computeHash(ProductStockView view);

    /**
     * Compares the current computed hash with a stored hash to check for changes.
     *
     * @param currentHash The newly computed hash.
     * @param storedHash  The hash retrieved from storage (e.g., metadata).
     * @return true if the hashes are equal, false otherwise.
     */
    boolean isUnchanged(String currentHash, String storedHash);

}

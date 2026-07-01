package com.nexus.rag.domain.service;

import com.nexus.rag.domain.model.ProductStockView;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class ProductHashCalculator {

    /**
     * Computes an SHA-256 hash based on the product's descriptive fields.
     *
     * @param view The product stock view to hash.
     * @return A hex-encoded string of the SHA-256 hash.
     */
    @NullMarked
    public String computeHash(ProductStockView view) {

        String canonical = view.getDescription() + "|" + view.getCategoryName() + "|" + view.getBrandName();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(canonical.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }

    }

    /**
     * Compares the current computed hash with a stored hash to check for changes.
     *
     * @param currentHash The newly computed hash.
     * @param storedHash  The hash retrieved from storage (e.g., metadata).
     * @return true if the hashes are equal, false otherwise.
     */
    @NullMarked
    public boolean isUnchanged(String currentHash, String storedHash) {
        return currentHash.equals(storedHash);
    }

}

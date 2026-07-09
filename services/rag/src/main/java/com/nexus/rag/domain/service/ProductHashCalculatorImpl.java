package com.nexus.rag.domain.service;

import com.nexus.rag.domain.model.ProductStockView;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class ProductHashCalculatorImpl implements ProductHashCalculator {

    @NullMarked
    @Override
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


    @NullMarked
    @Override
    public boolean isUnchanged(String currentHash, String storedHash) {
        return currentHash.equals(storedHash);
    }

}

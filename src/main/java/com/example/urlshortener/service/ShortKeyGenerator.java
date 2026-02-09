package com.example.urlshortener.service;

import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ShortKeyGenerator {
    
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MAX_RETRIES = 10;
    
    private final SecureRandom random = new SecureRandom();
    private final ShortUrlRepository repository;
    private final int keyLength;
    
    public ShortKeyGenerator(ShortUrlRepository repository,
                            @Value("${shortener.key.length:7}") int keyLength) {
        this.repository = repository;
        this.keyLength = keyLength;
    }
    
    /**
     * Generate a unique short key that doesn't exist in the database
     * @return A unique base62 encoded short key
     * @throws IllegalStateException if unable to generate unique key after max retries
     */
    public String generateUniqueKey() {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            String key = generateRandomKey();
            if (!repository.findByShortKey(key).isPresent()) {
                return key;
            }
            attempts++;
        }
        throw new IllegalStateException("Unable to generate unique key after " + MAX_RETRIES + " attempts");
    }
    
    /**
     * Generate a random base62 encoded key
     * @return A random key of the configured length
     */
    public String generateRandomKey() {
        StringBuilder key = new StringBuilder(keyLength);
        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(BASE62_CHARS.length());
            key.append(BASE62_CHARS.charAt(index));
        }
        return key.toString();
    }
}

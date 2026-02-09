package com.example.urlshortener.service;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {
    
    private final ShortUrlRepository repository;
    private final ShortKeyGenerator keyGenerator;
    
    public UrlShortenerService(ShortUrlRepository repository, ShortKeyGenerator keyGenerator) {
        this.repository = repository;
        this.keyGenerator = keyGenerator;
    }
    
    /**
     * Create a shortened URL
     * @param originalUrl The original URL to shorten
     * @param expiresAt Optional expiration date/time
     * @return The created ShortUrl entity
     */
    @Transactional
    public ShortUrl createShortUrl(String originalUrl, LocalDateTime expiresAt) {
        String shortKey = keyGenerator.generateUniqueKey();
        
        ShortUrl shortUrl = new ShortUrl(shortKey, originalUrl);
        shortUrl.setExpiresAt(expiresAt);
        
        return repository.save(shortUrl);
    }
    
    /**
     * Get the original URL by short key and increment visit count
     * @param shortKey The short key to look up
     * @return Optional containing the original URL if found and not expired
     */
    @Transactional
    public Optional<String> getOriginalUrl(String shortKey) {
        Optional<ShortUrl> shortUrlOpt = repository.findByShortKey(shortKey);
        
        if (shortUrlOpt.isEmpty()) {
            return Optional.empty();
        }
        
        ShortUrl shortUrl = shortUrlOpt.get();
        
        // Check if expired
        if (shortUrl.getExpiresAt() != null && LocalDateTime.now().isAfter(shortUrl.getExpiresAt())) {
            return Optional.empty();
        }
        
        // Increment visit count atomically
        shortUrl.incrementVisitCount();
        repository.save(shortUrl);
        
        return Optional.of(shortUrl.getOriginalUrl());
    }
    
    /**
     * Check if a short URL exists and is not expired
     * @param shortKey The short key to check
     * @return true if exists and not expired, false otherwise
     */
    public boolean exists(String shortKey) {
        Optional<ShortUrl> shortUrlOpt = repository.findByShortKey(shortKey);
        
        if (shortUrlOpt.isEmpty()) {
            return false;
        }
        
        ShortUrl shortUrl = shortUrlOpt.get();
        
        // Check if expired
        if (shortUrl.getExpiresAt() != null && LocalDateTime.now().isAfter(shortUrl.getExpiresAt())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if a short URL is expired
     * @param shortKey The short key to check
     * @return true if exists but expired, false otherwise
     */
    public boolean isExpired(String shortKey) {
        Optional<ShortUrl> shortUrlOpt = repository.findByShortKey(shortKey);
        
        if (shortUrlOpt.isEmpty()) {
            return false;
        }
        
        ShortUrl shortUrl = shortUrlOpt.get();
        
        return shortUrl.getExpiresAt() != null && LocalDateTime.now().isAfter(shortUrl.getExpiresAt());
    }
}

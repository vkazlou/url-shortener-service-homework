package com.example.urlshortener.dto;

import java.time.LocalDateTime;

public class CreateShortUrlResponse {
    
    private String shortKey;
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    
    public CreateShortUrlResponse() {
    }
    
    public CreateShortUrlResponse(String shortKey, String shortUrl, String originalUrl, 
                                 LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.shortKey = shortKey;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
    
    public String getShortKey() {
        return shortKey;
    }
    
    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }
    
    public String getShortUrl() {
        return shortUrl;
    }
    
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    
    public String getOriginalUrl() {
        return originalUrl;
    }
    
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}

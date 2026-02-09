package com.example.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {
    
    private String shortKey;
    private String shortUrl;
    private String originalUrl;
    private Long visitCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    
    public UrlStatsResponse() {
    }
    
    public UrlStatsResponse(String shortKey, String shortUrl, String originalUrl, 
                           Long visitCount, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.shortKey = shortKey;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.visitCount = visitCount;
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
    
    public Long getVisitCount() {
        return visitCount;
    }
    
    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
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

package com.example.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response containing the created shortened URL details")
public class CreateShortUrlResponse {
    
    @Schema(description = "The unique short key for the URL", example = "abc1234")
    private String shortKey;
    
    @Schema(description = "The complete shortened URL", example = "http://localhost:8080/abc1234")
    private String shortUrl;
    
    @Schema(description = "The original URL", example = "https://example.com/very/long/url")
    private String originalUrl;
    
    @Schema(description = "When the short URL was created", example = "2026-02-09T12:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "When the short URL expires (if set)", example = "2026-12-31T23:59:59")
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

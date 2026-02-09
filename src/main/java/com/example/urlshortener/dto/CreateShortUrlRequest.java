package com.example.urlshortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Request to create a shortened URL")
public class CreateShortUrlRequest {
    
    @Schema(description = "The original URL to be shortened", example = "https://example.com/very/long/url", required = true)
    private String originalUrl;
    
    @Schema(description = "Optional expiration date/time for the shortened URL", example = "2026-12-31T23:59:59")
    private LocalDateTime expiresAt;
    
    public CreateShortUrlRequest() {
    }
    
    public CreateShortUrlRequest(String originalUrl, LocalDateTime expiresAt) {
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
    }
    
    public String getOriginalUrl() {
        return originalUrl;
    }
    
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}

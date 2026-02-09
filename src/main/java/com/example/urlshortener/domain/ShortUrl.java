package com.example.urlshortener.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "short_url", indexes = {
    @Index(name = "idx_short_key", columnList = "shortKey", unique = true)
})
public class ShortUrl {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 10)
    private String shortKey;
    
    @Column(nullable = false, length = 2048)
    private String originalUrl;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private Long visitCount = 0L;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public ShortUrl() {
    }
    
    public ShortUrl(String shortKey, String originalUrl) {
        this.shortKey = shortKey;
        this.originalUrl = originalUrl;
        this.visitCount = 0L;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getShortKey() {
        return shortKey;
    }
    
    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
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
    
    public Long getVisitCount() {
        return visitCount;
    }
    
    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }
    
    public void incrementVisitCount() {
        this.visitCount++;
    }
}

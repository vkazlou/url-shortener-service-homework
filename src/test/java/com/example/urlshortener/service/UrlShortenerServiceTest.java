package com.example.urlshortener.service;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {
    
    @Mock
    private ShortUrlRepository repository;
    
    @Mock
    private ShortKeyGenerator keyGenerator;
    
    private UrlShortenerService service;
    
    @BeforeEach
    void setUp() {
        service = new UrlShortenerService(repository, keyGenerator);
    }
    
    @Test
    void createShortUrl_generatesKeyAndSavesEntity() {
        String originalUrl = "https://example.com/very/long/url";
        String generatedKey = "abc1234";
        
        when(keyGenerator.generateUniqueKey()).thenReturn(generatedKey);
        when(repository.save(any(ShortUrl.class))).thenAnswer(invocation -> {
            ShortUrl url = invocation.getArgument(0);
            url.setId(1L);
            return url;
        });
        
        ShortUrl result = service.createShortUrl(originalUrl, null);
        
        assertNotNull(result);
        assertEquals(generatedKey, result.getShortKey());
        assertEquals(originalUrl, result.getOriginalUrl());
        assertNull(result.getExpiresAt());
        
        verify(keyGenerator).generateUniqueKey();
        verify(repository).save(any(ShortUrl.class));
    }
    
    @Test
    void createShortUrl_withExpiration() {
        String originalUrl = "https://example.com";
        String generatedKey = "xyz9999";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        
        when(keyGenerator.generateUniqueKey()).thenReturn(generatedKey);
        when(repository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        ShortUrl result = service.createShortUrl(originalUrl, expiresAt);
        
        assertNotNull(result);
        assertEquals(generatedKey, result.getShortKey());
        assertEquals(expiresAt, result.getExpiresAt());
    }
    
    @Test
    void getOriginalUrl_returnsEmptyForMissingKey() {
        when(repository.findByShortKey("notfound")).thenReturn(Optional.empty());
        
        Optional<String> result = service.getOriginalUrl("notfound");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void getOriginalUrl_returnsEmptyForExpiredUrl() {
        String shortKey = "expired1";
        ShortUrl expiredUrl = new ShortUrl(shortKey, "https://example.com");
        expiredUrl.setExpiresAt(LocalDateTime.now().minusDays(1)); // Expired yesterday
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(expiredUrl));
        
        Optional<String> result = service.getOriginalUrl(shortKey);
        
        assertTrue(result.isEmpty());
        verify(repository, never()).save(any(ShortUrl.class));
    }
    
    @Test
    void getOriginalUrl_returnsUrlAndIncrementsVisitCount() {
        String shortKey = "valid123";
        String originalUrl = "https://example.com";
        ShortUrl shortUrl = new ShortUrl(shortKey, originalUrl);
        shortUrl.setVisitCount(5L);
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(shortUrl));
        when(repository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<String> result = service.getOriginalUrl(shortKey);
        
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get());
        
        // Verify visit count was incremented
        ArgumentCaptor<ShortUrl> captor = ArgumentCaptor.forClass(ShortUrl.class);
        verify(repository).save(captor.capture());
        assertEquals(6L, captor.getValue().getVisitCount());
    }
    
    @Test
    void getOriginalUrl_withNonExpiredUrl() {
        String shortKey = "valid999";
        String originalUrl = "https://example.com";
        ShortUrl shortUrl = new ShortUrl(shortKey, originalUrl);
        shortUrl.setExpiresAt(LocalDateTime.now().plusDays(7)); // Expires in 7 days
        shortUrl.setVisitCount(0L);
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(shortUrl));
        when(repository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Optional<String> result = service.getOriginalUrl(shortKey);
        
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get());
        verify(repository).save(any(ShortUrl.class));
    }
    
    @Test
    void isExpired_returnsTrueForExpiredUrl() {
        String shortKey = "expired1";
        ShortUrl expiredUrl = new ShortUrl(shortKey, "https://example.com");
        expiredUrl.setExpiresAt(LocalDateTime.now().minusDays(1));
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(expiredUrl));
        
        assertTrue(service.isExpired(shortKey));
    }
    
    @Test
    void isExpired_returnsFalseForValidUrl() {
        String shortKey = "valid123";
        ShortUrl validUrl = new ShortUrl(shortKey, "https://example.com");
        validUrl.setExpiresAt(LocalDateTime.now().plusDays(7));
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(validUrl));
        
        assertFalse(service.isExpired(shortKey));
    }
    
    @Test
    void exists_returnsTrueForValidUrl() {
        String shortKey = "valid123";
        ShortUrl validUrl = new ShortUrl(shortKey, "https://example.com");
        
        when(repository.findByShortKey(shortKey)).thenReturn(Optional.of(validUrl));
        
        assertTrue(service.exists(shortKey));
    }
    
    @Test
    void exists_returnsFalseForMissingUrl() {
        when(repository.findByShortKey("notfound")).thenReturn(Optional.empty());
        
        assertFalse(service.exists("notfound"));
    }
}

package com.example.urlshortener.service;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortKeyGeneratorTest {
    
    @Mock
    private ShortUrlRepository repository;
    
    private ShortKeyGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new ShortKeyGenerator(repository, 7);
    }
    
    @Test
    void generatedKeysHaveCorrectLength() {
        when(repository.findByShortKey(anyString())).thenReturn(Optional.empty());
        
        String key = generator.generateUniqueKey();
        
        assertEquals(7, key.length(), "Generated key should have length 7");
    }
    
    @Test
    void generatedKeysMatchBase62Regex() {
        when(repository.findByShortKey(anyString())).thenReturn(Optional.empty());
        
        String key = generator.generateUniqueKey();
        
        assertTrue(key.matches("^[0-9A-Za-z]+$"), 
                   "Generated key should only contain alphanumeric characters (base62)");
    }
    
    @Test
    void generateThousandKeysWithoutDuplicates() {
        Set<String> generatedKeys = new HashSet<>();
        
        for (int i = 0; i < 1000; i++) {
            String key = generator.generateRandomKey();
            generatedKeys.add(key);
        }
        
        assertEquals(1000, generatedKeys.size(), 
                    "All 1000 generated keys should be unique");
    }
    
    @Test
    void retriesWhenCollisionOccurs() {
        ShortUrl existingUrl = new ShortUrl();
        existingUrl.setShortKey("abc1234");
        
        // First call returns collision, second call returns empty (no collision)
        when(repository.findByShortKey(anyString()))
            .thenReturn(Optional.of(existingUrl))
            .thenReturn(Optional.empty());
        
        String key = generator.generateUniqueKey();
        
        assertNotNull(key, "Should eventually generate a unique key after collision");
        assertEquals(7, key.length());
    }
    
    @Test
    void throwsExceptionAfterMaxRetries() {
        ShortUrl existingUrl = new ShortUrl();
        existingUrl.setShortKey("collision");
        
        // Always return a collision
        when(repository.findByShortKey(anyString()))
            .thenReturn(Optional.of(existingUrl));
        
        assertThrows(IllegalStateException.class, 
                    () -> generator.generateUniqueKey(),
                    "Should throw exception when unable to generate unique key after max retries");
    }
}

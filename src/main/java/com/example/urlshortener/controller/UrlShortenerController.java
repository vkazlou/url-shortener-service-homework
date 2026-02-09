package com.example.urlshortener.controller;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.dto.CreateShortUrlRequest;
import com.example.urlshortener.dto.CreateShortUrlResponse;
import com.example.urlshortener.dto.UrlStatsResponse;
import com.example.urlshortener.exception.UrlExpiredException;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class UrlShortenerController {
    
    private final UrlShortenerService service;
    private final ShortUrlRepository repository;
    
    public UrlShortenerController(UrlShortenerService service, ShortUrlRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    @PostMapping("/api/urls")
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(
            @RequestBody CreateShortUrlRequest request,
            HttpServletRequest httpRequest) {
        
        if (request.getOriginalUrl() == null || request.getOriginalUrl().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        ShortUrl shortUrl = service.createShortUrl(request.getOriginalUrl(), request.getExpiresAt());
        
        // Build the short URL
        String baseUrl = getBaseUrl(httpRequest);
        String shortUrlString = baseUrl + "/" + shortUrl.getShortKey();
        
        CreateShortUrlResponse response = new CreateShortUrlResponse(
            shortUrl.getShortKey(),
            shortUrlString,
            shortUrl.getOriginalUrl(),
            shortUrl.getCreatedAt(),
            shortUrl.getExpiresAt()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey) {
        // Check if expired first
        if (service.isExpired(shortKey)) {
            throw new UrlExpiredException("Short URL has expired");
        }
        
        Optional<String> originalUrl = service.getOriginalUrl(shortKey);
        
        if (originalUrl.isEmpty()) {
            throw new UrlNotFoundException("Short URL not found");
        }
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl.get()))
                .build();
    }
    
    @GetMapping("/api/urls/{shortKey}")
    public ResponseEntity<UrlStatsResponse> getStats(
            @PathVariable String shortKey,
            HttpServletRequest httpRequest) {
        
        Optional<ShortUrl> shortUrlOpt = repository.findByShortKey(shortKey);
        
        if (shortUrlOpt.isEmpty()) {
            throw new UrlNotFoundException("Short URL not found");
        }
        
        ShortUrl shortUrl = shortUrlOpt.get();
        
        String baseUrl = getBaseUrl(httpRequest);
        String shortUrlString = baseUrl + "/" + shortUrl.getShortKey();
        
        UrlStatsResponse response = new UrlStatsResponse(
            shortUrl.getShortKey(),
            shortUrlString,
            shortUrl.getOriginalUrl(),
            shortUrl.getVisitCount(),
            shortUrl.getCreatedAt(),
            shortUrl.getExpiresAt()
        );
        
        return ResponseEntity.ok(response);
    }
    
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        return url.toString();
    }
}

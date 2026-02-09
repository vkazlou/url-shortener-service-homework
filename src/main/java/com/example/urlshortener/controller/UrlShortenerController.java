package com.example.urlshortener.controller;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.dto.CreateShortUrlRequest;
import com.example.urlshortener.dto.CreateShortUrlResponse;
import com.example.urlshortener.dto.UrlStatsResponse;
import com.example.urlshortener.exception.UrlExpiredException;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@Tag(name = "URL Shortener", description = "API for creating and managing shortened URLs")
public class UrlShortenerController {
    
    private final UrlShortenerService service;
    private final ShortUrlRepository repository;
    
    public UrlShortenerController(UrlShortenerService service, ShortUrlRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    @PostMapping("/api/urls")
    @Operation(summary = "Create a shortened URL", 
               description = "Creates a new shortened URL for the provided original URL with optional expiration date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Short URL created successfully",
                    content = @Content(schema = @Schema(implementation = CreateShortUrlResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request - URL is empty or blank",
                    content = @Content)
    })
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(
            @Parameter(description = "Request containing the original URL and optional expiration date")
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
    @Operation(summary = "Redirect to original URL", 
               description = "Redirects to the original URL associated with the short key and increments visit count")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirect to original URL"),
        @ApiResponse(responseCode = "404", description = "Short URL not found",
                    content = @Content),
        @ApiResponse(responseCode = "410", description = "Short URL has expired",
                    content = @Content)
    })
    public ResponseEntity<Void> redirect(
            @Parameter(description = "The short key to redirect")
            @PathVariable String shortKey) {
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
    @Operation(summary = "Get URL statistics", 
               description = "Retrieves statistics for a shortened URL including visit count and creation time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UrlStatsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Short URL not found",
                    content = @Content)
    })
    public ResponseEntity<UrlStatsResponse> getStats(
            @Parameter(description = "The short key to get statistics for")
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

# Swagger API Documentation

This project includes comprehensive OpenAPI/Swagger documentation for all REST endpoints.

## Accessing Swagger UI

Once the application is running, you can access the interactive API documentation at:

**Swagger UI:** http://localhost:8080/swagger-ui.html

**OpenAPI JSON:** http://localhost:8080/api-docs

## Features

The Swagger documentation includes:

- **Complete endpoint documentation** with descriptions, parameters, and response codes
- **Interactive API testing** - Try out endpoints directly from the browser
- **Request/Response examples** with schema definitions
- **Error response documentation** (400, 404, 410)

## Documented Endpoints

### POST /api/urls
Create a shortened URL
- **Request:** Original URL and optional expiration date
- **Response:** 201 Created with shortKey, shortUrl, and originalUrl
- **Errors:** 400 Bad Request for invalid input

### GET /{shortKey}
Redirect to original URL
- **Response:** 302 Found redirect to original URL
- **Errors:** 
  - 404 Not Found if shortKey doesn't exist
  - 410 Gone if URL has expired

### GET /api/urls/{shortKey}
Get URL statistics
- **Response:** 200 OK with visit count and metadata
- **Errors:** 404 Not Found if shortKey doesn't exist

## Configuration

Swagger settings are configured in `application.yml`:
- API title: "URL Shortener Service API"
- Version: 1.0.0
- Operations sorted by HTTP method
- Tags sorted alphabetically

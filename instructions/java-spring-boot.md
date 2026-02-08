# Java / Spring Boot Instructions

## Project Requirements
- Java 25
- Spring Boot 4
- REST API with Spring Web
- Persistence with Spring Data JPA
- H2 Database for storage
- Lombok for boilerplate reduction (optional)

## Basic Structure
- Controller layer for REST endpoints
- Service layer for business logic
- Repository layer for data access
- Entity classes for JPA mapping

## Configuration
- Use `application.yml` (or `application.properties`)
- H2 in-memory is acceptable for MVP
- Enable JPA auto DDL for local development

## REST Behavior
- `POST /api/urls` creates a short URL
- `GET /{shortKey}` redirects to the original URL
- Optional stats endpoint

## Validation
- Validate `originalUrl` with bean validation (`@URL` or custom)
- Return 400 for invalid requests

## Error Codes
- 404 for unknown `shortKey`
- 410 for expired links

## Build & Run
- Build: `mvn clean package`
- Run: `mvn spring-boot:run`

# Java Maven Integration Test Instructions

## Scope
Write integration tests that start Spring context and test REST endpoints.

### What to Test
- `POST /api/urls`
  - returns 201 for valid URL
  - returns 400 for invalid URL
- `GET /{shortKey}`
  - redirects (302/307) for valid and not expired key
  - returns 404 for missing key
  - returns 410 for expired key
- Optional: `GET /api/urls/{shortKey}` for stats

## Recommended Dependencies
Add to `pom.xml`:
- `spring-boot-starter-test`

## Structure & Naming
- Location: `src/test/java`
- Suffix: `*IT`

## Setup
- Use `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
- Use `TestRestTemplate` (or `WebTestClient` if preferred)
- Use H2 in-memory (default) for tests

## Maven Command
Run integration tests only:
```
mvn -Dtest=*IT test
```

## Run All Tests
```
mvn test
```

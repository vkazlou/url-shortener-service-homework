# Tasks (MVP Execution Plan)

## 1) Project Setup
- [ ] Create Spring Boot 4 Maven project with Java 25
- [ ] Add dependencies: Web, Data JPA, H2, Lombok, Test
- [ ] Configure `application.yml` for H2 and JPA

## 2) Domain & Persistence
- [ ] Create `ShortUrl` entity with fields: `id`, `shortKey`, `originalUrl`, `createdAt`, `expiresAt`, `visitCount`
- [ ] Add unique index/constraint on `shortKey`
- [ ] Create JPA repository with `findByShortKey`

## 3) Key Generation
- [ ] Implement base62 random `ShortKeyGenerator`
- [ ] Enforce key length (start with 7)
- [ ] Add collision check with retry

## 4) Service Layer
- [ ] Implement create-short-URL logic
- [ ] Implement redirect lookup and expiration check
- [ ] Increment visit count atomically

## 5) REST API
- [ ] `POST /api/urls` create endpoint
- [ ] `GET /{shortKey}` redirect endpoint
- [ ] Optional `GET /api/urls/{shortKey}` stats endpoint
- [ ] Map errors: 404 not found, 410 expired, 400 validation

## 6) Validation & Error Handling
- [ ] Validate `originalUrl` format
- [ ] Validate `expiresAt` (must be in future if provided)
- [ ] Add global exception handler

## 7) Testing
### Unit Tests
- [ ] `ShortKeyGenerator` tests
- [ ] Service logic tests
- [ ] Validation tests

### Integration Tests
- [ ] `POST /api/urls` happy + invalid
- [ ] `GET /{shortKey}` redirect + expired + not found
- [ ] Optional stats endpoint

## 8) Optional Caching (Suggestion)
- [ ] Add Caffeine cache for `shortKey -> originalUrl`
- [ ] Set TTL and negative caching
- [ ] Evict on update/delete

## 9) Documentation
- [ ] Update README or instructions with run steps
- [ ] Add example requests

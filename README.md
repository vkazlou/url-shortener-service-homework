# URL Shortener Service (MVP)

Build a REST API similar to Bitly.

## Stack
- Java 25
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Lombok
- H2 Database

## Goals
Implement an MVP URL shortener with:
- Short URL generation
- Redirect to original URL
- Visit counter tracking
- Optional expiration for short links

## Functional Requirements
### 1) Create Short URL
- **Endpoint:** `POST /api/urls`
- **Request body (JSON):**
  - `originalUrl` (string, required, valid URL)
  - `expiresAt` (ISO-8601 datetime, optional)
- **Response (201):**
  - `id` (UUID or Long)
  - `shortKey` (string, random)
  - `shortUrl` (string, full URL including host)
  - `originalUrl` (string)
  - `expiresAt` (datetime or null)
  - `createdAt` (datetime)
  - `visitCount` (long)

### 2) Redirect
- **Endpoint:** `GET /{shortKey}`
- **Behavior:**
  - If `shortKey` exists and not expired: increment visit count and redirect (HTTP 302 or 307) to `originalUrl`.
  - If `shortKey` not found: return 404.
  - If expired: return 410 (Gone).

### 3) Retrieve Stats (optional for MVP)
- **Endpoint:** `GET /api/urls/{shortKey}`
- **Response:** same fields as create response.

## Non-Functional Requirements
- Use H2 for persistence (file or in-memory is fine).
- Random short key generation (base62 or similar).
- Ensure short key uniqueness.
- Basic validation for URL format.
- Track visit count atomically.

## Data Model (suggestion)
`ShortUrl` entity:
- `id`
- `shortKey` (unique)
- `originalUrl`
- `createdAt`
- `expiresAt` (nullable)
- `visitCount`

## Copilot Focus (Implementation Hints)
### Random Key Generation
- Use base62 alphabet (`0-9A-Za-z`).
- Key length 6–10 (start with 7).
- Generate, then check for collisions in DB; retry if exists.

### Redirect Handling
- For `GET /{shortKey}`: load entity by `shortKey`.
- Validate expiration; if expired return 410.
- Increment `visitCount` and save.
- Return redirect response to `originalUrl`.

### Caching Suggestions
- For high traffic, cache `shortKey -> originalUrl` (e.g., Caffeine) with TTL.
- Cache negative lookups briefly to avoid DB thrash.
- Evict cache entries on update/delete.

## Deliverables
- Spring Boot app with REST controllers, service layer, repository layer.
- H2 configuration and schema generation via JPA.
- README with run instructions and example requests.

## Example Requests
### Create short URL
```
POST /api/urls
Content-Type: application/json

{
  "originalUrl": "https://example.com/some/long/path",
  "expiresAt": "2026-12-31T23:59:59Z"
}
```

### Redirect
```
GET /abc1234
```

---
If you want additional endpoints (delete, custom alias, pagination), add them after MVP.

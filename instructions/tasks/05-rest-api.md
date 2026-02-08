# Task 05 — REST API

- `POST /api/urls` create endpoint
- `GET /{shortKey}` redirect endpoint
- Optional `GET /api/urls/{shortKey}` stats endpoint
- Map errors: 404 not found, 410 expired, 400 validation

## Acceptance Criteria
- `POST /api/urls` returns 201 and response contains `shortKey`, `shortUrl` and `originalUrl`.
- `GET /{shortKey}` responds with 302/307 redirect for valid key.
- Invalid or missing keys return 404; expired keys return 410; invalid input returns 400.

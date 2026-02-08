# Task 08 — Integration Tests

- `POST /api/urls` happy + invalid
- `GET /{shortKey}` redirect + expired + not found
- Optional stats endpoint

## Acceptance Criteria
- `mvn -Dtest=*IT test` completes successfully.
- Integration tests assert correct HTTP status codes and redirect behavior.

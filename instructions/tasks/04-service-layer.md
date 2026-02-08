# Task 04 — Service Layer

- Implement create-short-URL logic
- Implement redirect lookup and expiration check
- Increment visit count atomically

## Acceptance Criteria
- Creating a short URL returns a non-empty `shortKey` and stored entity.
- Redirect lookup returns 404 for missing key and 410 for expired key.
- Visit count increases by 1 for each successful redirect.

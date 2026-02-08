# Task 03 — Key Generation

- Implement base62 random `ShortKeyGenerator`
- Enforce key length (start with 7)
- Add collision check with retry

## Acceptance Criteria
- Generated keys are length 7 and match the regex `^[0-9A-Za-z]+$`.
- Generating 1,000 keys yields no duplicates in a unit test.
- Collision handling retries when a key already exists in DB (verified by a mocked repository test).

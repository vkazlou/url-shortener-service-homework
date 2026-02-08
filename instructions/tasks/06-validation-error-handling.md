# Task 06 — Validation & Error Handling

- Validate `originalUrl` format
- Validate `expiresAt` (must be in future if provided)
- Add global exception handler

## Acceptance Criteria
- Invalid URL input returns 400 with a clear error message.
- `expiresAt` in the past returns 400 with a clear error message.
- Unhandled errors are converted to a consistent JSON error response.

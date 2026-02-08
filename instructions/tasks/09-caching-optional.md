# Task 09 — Optional Caching

- Add Caffeine cache for `shortKey -> originalUrl`
- Set TTL and negative caching
- Evict on update/delete

## Acceptance Criteria
- Cache hits reduce database lookups for repeated redirects (verified with logs or metrics).
- Expired or deleted keys are not served from cache.

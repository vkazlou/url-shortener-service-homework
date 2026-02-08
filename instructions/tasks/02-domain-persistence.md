# Task 02 — Domain & Persistence

- Create `ShortUrl` entity with fields: `id`, `shortKey`, `originalUrl`, `createdAt`, `expiresAt`, `visitCount`
- Add unique index/constraint on `shortKey`
- Create JPA repository with `findByShortKey`

## Acceptance Criteria
- Application starts without JPA mapping errors.
- Database schema contains a table for `ShortUrl` with a unique constraint on `shortKey`.
- Repository can persist and load a `ShortUrl` by `shortKey` in a simple test.

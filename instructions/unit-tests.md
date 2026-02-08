# Java Maven Unit Test Instructions

## Scope
Write unit tests for core business logic without starting the full Spring context.

### What to Test
- `ShortKeyGenerator`
  - length is within expected range
  - uses only base62 alphabet (`0-9A-Za-z`)
  - generates different values across multiple calls
- Service layer
  - create short URL for valid input
  - reject invalid URL
  - expire logic when `expiresAt` is in the past
  - visit count increments on redirect
- Validation
  - invalid URL formats are rejected

## Recommended Dependencies
Add to `pom.xml`:
- `spring-boot-starter-test` (includes JUnit 5, AssertJ, Mockito)

## Structure & Naming
- Location: `src/test/java`
- Suffix: `*Test`
- Package mirrors production packages

## Test Style
- JUnit 5 (`@Test`, `@BeforeEach`)
- Use Mockito for collaborators and repositories
- Keep tests isolated from DB and web layer

## Maven Command
Run unit tests only:
```
mvn -Dtest=*Test test
```

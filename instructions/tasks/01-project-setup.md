# Task 01 — Project Setup

- Create Spring Boot 4 Maven project with Java 25
- Add dependencies: Web, Data JPA, H2, Lombok, Test
- Configure `application.yml` for H2 and JPA

## Acceptance Criteria
- `mvn -v` shows Java 25 is used.
- `mvn test` completes without dependency errors.
- App starts with `mvn spring-boot:run` and logs show H2 + JPA are initialized.

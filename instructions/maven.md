# Maven Instructions

## Project Setup
- Use Maven as the build tool.
- Packaging: `jar`
- Java version: 25

## Required Dependencies (MVP)
Add to `pom.xml`:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `com.h2database:h2`
- `org.projectlombok:lombok` (optional, `provided` scope)
- `spring-boot-starter-test` (test scope)

## Recommended Plugins
- `spring-boot-maven-plugin`
- `maven-compiler-plugin` with `release=25`

## Common Maven Commands
- Build: `mvn clean package`
- Run tests: `mvn test`
- Run app: `mvn spring-boot:run`

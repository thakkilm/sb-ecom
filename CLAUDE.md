# Project: sb-ecom

Spring Boot e-commerce application (Java 17, Maven). This file gives Claude
persistent context that isn't obvious from reading the code.

## Build & run
- Build: `./mvnw clean install`
- Run: `./mvnw spring-boot:run`
- Run a single test: `./mvnw test -Dtest=ClassName#methodName`
- Run all tests: `./mvnw test`
- Lint/format: `<your linter command, e.g. ./mvnw spotless:apply>`

## Code style
- Java 17, standard Google/Sun conventions unless noted
- Base package: `com.ecommerce.project.sbecom`
- Package by layer: `controller/`, `service/`, `repository/`, `model/`, `dto/`
- Constructor injection only — no field `@Autowired`
- DTOs for controller boundaries; never expose JPA entities directly in API responses
- Prefer `Optional<T>` over returning null from service methods

## Testing
- JUnit 5 + Mockito. Assertions via AssertJ (`assertThat(...)`), not raw JUnit asserts
- Test naming: `methodName_condition_expectedResult`
- Unit tests mock dependencies; integration tests use `@SpringBootTest` + Testcontainers
  where a real DB/service is involved
- Run the full test suite before committing; don't commit with failing tests

## Git workflow
- Branch naming: `feature/<short-desc>`, `fix/<short-desc>`, `chore/<short-desc>`
- Commit messages follow **Conventional Commits**:
  - Format: `<type>(<scope>): <short summary>`
  - Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `perf`, `build`, `ci`
  - Example: `feat(orders): add idempotency key to order creation endpoint`
  - Example: `fix(auth): correct token expiry check off-by-one`
  - Body (optional): explain *why*, not *what* — the diff already shows what
  - Keep summary line under 72 chars, imperative mood ("add", not "added")
- One logical change per commit. Don't bundle unrelated file changes together.
- Before committing: run tests, review the diff, then write the message —
  don't let Claude commit without you having seen the diff first.
- Never commit secrets, `.env` files, or credentials. Check `git diff --staged`
  for accidental inclusions before pushing.

## Architecture notes
- <e.g. "We use hexagonal architecture: domain layer has no Spring dependencies">
- <e.g. "All external API calls go through the `client/` package, never called directly from services">
- <e.g. "Feature flags are managed via <tool>; check before assuming a code path is live">

## Gotchas
- <anything non-obvious that's bitten you before — env vars needed locally, a
  flaky test, a service that needs to be running for integration tests, etc.>

---
Run `/init` inside Claude Code once you're in your actual repo — it'll scan
your build files and fill in the specifics above automatically. Treat this as
a living document: prune anything Claude already does right without being told,
and add a line the moment Claude gets something wrong twice.

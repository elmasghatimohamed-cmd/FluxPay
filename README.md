# FluxPay

Educational distributed transfer platform using fictional MAD balances. **No real money, banking integrations, or production-grade financial claims.**

## Services
- **Financial Core** (8081): future wallets, reservations and immutable double-entry ledger.
- **Transfer Service** (8082): future transfer lifecycle and durable Saga orchestration.
- **Risk Service** (8083): future risk assessments.
- **Notification Service** (8084): future asynchronous internal notifications.

The four Spring Boot applications currently provide startup and health probes; the business capabilities listed above are planned, not implemented.

## Technical baseline
- Microsoft OpenJDK 25.0.5.0.2; Spring Boot 4.1.2; Maven Wrapper (Maven 3.9.16)
- PostgreSQL 18.7 and Apache Kafka 4.2.2 via Docker Compose
- Monorepo with independent services and service-specific databases

## Local setup
Prerequisites: Java 25, Docker Desktop/Engine with Compose, and a terminal. The Maven Wrapper avoids requiring a separate Maven installation.

1. Copy the example environment file to a local ignored file: `Copy-Item .env.example .env` (PowerShell) or `cp .env.example .env` (Bash).
2. Replace all `CHANGE_ME_...` values with unique local secrets. Do not commit `.env`.
3. Validate and start infrastructure from the repository root:

```powershell
docker compose --env-file .env -f infrastructure/compose/compose.yaml config --quiet
docker compose --env-file .env -f infrastructure/compose/compose.yaml up -d --wait
docker compose --env-file .env -f infrastructure/compose/compose.yaml ps
```

4. Build and test all backend modules:

```powershell
.\mvnw.cmd clean verify
```

5. Start a service in a separate terminal (example: Financial Core):

```powershell
.\mvnw.cmd -pl services/financial-core spring-boot:run -Dspring-boot.run.profiles=dev
```

6. Check its health at http://127.0.0.1:8081/actuator/health. Other default service ports: 8082, 8083, 8084. Start each with the corresponding `-pl services/<directory>` value.

To stop the infrastructure without deleting data:

```powershell
docker compose --env-file .env -f infrastructure/compose/compose.yaml down
```

**Warning:** `down --volumes` deletes the local PostgreSQL and Kafka volumes. Use it only when you intentionally want a clean reset. Changing database credentials in `.env` does not automatically reinitialize existing database volumes.

## CI
- **Backend CI:** Maven `clean verify` for all services.
- **Infrastructure CI:** isolated PostgreSQL logins and Kafka publish/consume smoke tests with temporary CI credentials.
- **Qodana:** JVM static analysis.

These workflows passed on the foundation feature branch before integration into `develop`. They validate the technical scaffold, not financial business logic.

## Structure and collaboration
- `services/`: backend applications.
- `infrastructure/`: Docker Compose and PostgreSQL initialization.
- `contracts/`: HTTP and event contract conventions.
- `docs/adr/`: architecture decisions.
- `main`: stable releases; `develop`: integration; scoped branches for changes.
- Conventional Commits and pull requests for significant changes.

## Milestones
- **P0 foundation:** service scaffolding, health probes, isolated infrastructure and CI completed; local reproducibility still requires developer verification.
- **P1 Financial Core:** define and implement wallet/ledger invariants, reservations, idempotency and concurrency tests before distributed transfer workflows.

See [ADR-001](docs/adr/0001-foundation-architecture.md) and [contract conventions](contracts/README.md).

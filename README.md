# FluxPay

Educational distributed transfer platform using fictional MAD balances.
No real money or banking connections.

## Business services

- Transfer Service: transfer lifecycle and durable Saga orchestration.
- Financial Core: wallets, reservations and immutable double-entry ledger.
- Risk Service: risk assessments.
- Notification Service: asynchronous internal notifications.

## Technical baseline

- Microsoft OpenJDK 25.0.5.0.2
- Spring Boot 4.1.2
- Maven 3.9.16
- PostgreSQL 18.7
- Apache Kafka 4.2.2

## Repository

- services/: independently deployable backend applications.
- frontend/: React and TypeScript application.
- infrastructure/: local infrastructure configuration.
- contracts/: HTTP and messaging contracts.
- tests/: cross-service verification.
- docs/: architecture, decisions and operations.

## Development workflow

- main: stable releases.
- develop: integration.
- feature/*, fix/*, test/*, docs/*, refactor/*, chore/*: scoped changes.
- Conventional Commits.
- Pull requests for significant changes.

## Current status

P0 technical foundations are being initialized.
Applications, infrastructure and CI are not runnable yet.

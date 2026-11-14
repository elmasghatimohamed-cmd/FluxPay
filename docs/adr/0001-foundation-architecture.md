# ADR-001: Foundation architecture and boundaries

Status: Accepted for P0 baseline (2026-11-14)

## Context
FluxPay is an educational distributed transfer simulation using fictional MAD balances. It must not handle real funds or connect to banking rails.

## Decisions
- Keep one repository with four independently deployable Spring Boot applications: financial-core, transfer-service, risk-service, and notification-service.
- Financial Core owns wallet balances, reservations and the immutable double-entry ledger. Other services must not write its financial records.
- Transfer Service owns transfer lifecycle and durable Saga orchestration; distributed workflows are implemented after the Financial Core invariants.
- Risk Service owns risk assessments. Notification Service owns asynchronous internal notifications.
- Each service has its own PostgreSQL database and login. Cross-service database access is forbidden.
- Kafka provides asynchronous messaging. HTTP contracts and event schemas must be versioned before their business implementation.
- P0 infrastructure is local-development oriented; Kafka's PLAINTEXT listeners and example environment values are not production security defaults.
- Use Git branches main/develop with scoped feature branches, Conventional Commits and CI checks.

## Consequences
- Cross-service operations must use explicit APIs or events, never direct database joins.
- P1 must define money precision, ledger balancing, transaction isolation, idempotency, reservation lifecycle and concurrency tests before accepting transfer workflows.
- No P0 smoke test proves financial correctness; those tests belong to P1 and later milestones.

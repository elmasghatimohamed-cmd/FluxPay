# Contract baseline (P0)

This directory reserves the source-controlled location for service HTTP and Kafka contracts. No business endpoints or event schemas have been implemented yet.

## HTTP conventions
- Document each public endpoint using OpenAPI before implementation.
- Specify request/response schemas, validation constraints, status codes, error structure and idempotency behavior.
- Keep internal health probes separate from business APIs.

## Messaging conventions
- Version every event schema explicitly.
- Define event ID, event type, schema version, timestamp, producer, correlation/causation identifiers, payload and delivery semantics.
- Consumers must handle duplicate deliveries; message ordering and retry/dead-letter behavior must be defined per event.
- Do not treat Kafka publication as proof of exactly-once financial execution.

## Next milestone
Define concrete Financial Core contracts for wallet creation, balance lookup, reservation, release and ledger posting as part of P1. Do not assume these APIs exist during P0.

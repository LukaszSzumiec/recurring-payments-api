# Recurring Payments API 💳⏱️

A showcase project built with Java 21 and Spring Boot 3 demonstrating a clean Hexagonal Architecture for managing recurring subscriptions and payments.

The application covers real-life recurring billing challenges:
- subscription lifecycle management
- scheduled billing with retry rules
- idempotency for payment processing
- event outbox for reliable event publishing
- database migrations (Flyway)
- API docs with OpenAPI (Swagger UI)
- Docker & Testcontainers setup for CI/CD

------------------------------------------------------------
🏗️ Architecture
------------------------------------------------------------

Layers:
- domain → pure business model (Subscription, Payment, User)
- application → orchestrates use cases (ProcessPaymentService, CreateSubscriptionService)
- infrastructure → JPA repositories, schedulers, adapters, config
- api → REST controllers

Flow:
1. REST API Controllers handle HTTP requests.
2. Application Services execute business use cases.
3. Domain Model encapsulates business rules.
4. Ports define interfaces.
5. Adapters implement ports using JPA, Outbox, Idempotency.
6. PostgreSQL + Flyway for persistence.
7. Scheduled tasks:
   - PaymentScheduler for due charges.
   - OutboxPublisher for reliable event publishing.

------------------------------------------------------------
🛠️ Technology Stack
------------------------------------------------------------

- Java 21 (Records, Pattern Matching, Virtual Threads ready)
- Spring Boot 3.4
- Spring Data JPA (Postgres)
- Flyway (migrations)
- Springdoc OpenAPI (Swagger UI)
- Spring Security (JWT-ready, minimal config)
- Testcontainers (integration tests)
- Docker Compose (local dev environment)

------------------------------------------------------------
🚀 Getting Started
------------------------------------------------------------

Prerequisites:
- JDK 21
- Maven 3.9+
- Docker & Docker Compose

Run with Docker Compose:
    docker compose up --build

App runs at: http://localhost:8080
Swagger UI:  http://localhost:8080/swagger-ui.html

Run locally (with Maven):
    mvn clean spring-boot:run

------------------------------------------------------------
📚 Example API Usage
------------------------------------------------------------

1. Create subscription:
   curl -X POST http://localhost:8080/api/subscriptions \
     -H "Content-Type: application/json" \
     -d '{"userId":1,"price":19.99,"billingDayOfMonth":1}'

2. Trigger payment processing (idempotent):
   curl -X POST http://localhost:8080/api/subscriptions/1/process \
     -H "Idempotency-Key: abc123"

3. List user payments:
   curl http://localhost:8080/api/subscriptions/user/1/payments

------------------------------------------------------------
✅ CI/CD
------------------------------------------------------------

GitHub Actions workflow:
- builds project on push & PR
- runs unit + integration tests (Testcontainers)

------------------------------------------------------------
🌟 Next Steps
------------------------------------------------------------

- JWT authentication & roles
- Rate limiting (Resilience4j / Bucket4j)
- Webhook simulation (mock payment provider)
- Grace period & retry rules
- Metrics & observability (Micrometer, Actuator, Prometheus)

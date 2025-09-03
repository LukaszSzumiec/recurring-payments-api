# Recurring Payments API 💳⏱️

A showcase project built with **Java 21** and **Spring Boot 3** demonstrating a clean Hexagonal Architecture for managing recurring subscriptions and payments.

The application covers real-life recurring billing challenges:
- subscription lifecycle management
- scheduled billing with retry rules
- idempotency for payment processing
- **event outbox for reliable event publishing**
- database migrations (Flyway)
- API docs with OpenAPI (Swagger UI)
- Docker & Testcontainers setup for CI/CD

------------------------------------------------------------
🏗️ Architecture
------------------------------------------------------------

Layers:
- **domain.model** → core JPA entities (`User`, `Subscription`, `Payment`, `OutboxEvent`, `PaymentStatus`)
- **domain.port** → repository interfaces (ports)
- **application** → service interfaces (`CreateSubscriptionService`, `PaymentProcessingService`, `GetPaymentsService`)
- **application/impl** → service implementations
- **application/dto** → request/response DTOs
- **infrastructure** → adapters (JPA repositories, OutboxRepositoryAdapter), schedulers, config, security
- **api** → REST controllers

Flow:
1. REST API Controllers handle HTTP requests.
2. Application Services execute business logic.
3. Domain Model stores data as JPA entities.
4. Ports define contracts.
5. Adapters implement ports (JPA, Outbox).
6. PostgreSQL + Flyway handle persistence.
7. Scheduled tasks:
   - `PaymentScheduler` for due subscription charges
   - `OutboxPublisher` for reliable event publishing

------------------------------------------------------------
🛠️ Technology Stack
------------------------------------------------------------

- Java 21 (Records, Pattern Matching, Virtual Threads ready)
- Spring Boot 3.4
- JPA with Jakarta + Lombok
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

2. Trigger payment processing:
   curl -X POST http://localhost:8080/api/subscriptions/1/process

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

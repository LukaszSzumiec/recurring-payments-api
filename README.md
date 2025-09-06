# 🚀 Recurring Payments API

> 🗄️ **Database** • ☕ **Spring Boot** • ⏱️ **Scheduler** • 📤 **Events (Outbox)** • 🧩 **Idempotency** • 🔌 **API (OpenAPI)** • ✅ **Tests**

A modern, schema-first REST API for subscriptions and recurring payments. Includes JWT auth (access + rotating refresh), scheduled billing, an **Outbox** publisher for reliable events, and idempotent write operations.

---

## ✨ Highlights

* 🗄️ **PostgreSQL + Flyway + JPA/Hibernate** (`ddl-auto: validate`)
* ☕ **Spring Boot 3** (Java 21, virtual threads)
* ⏱️ **Schedulers**: billing + outbox publishing (CRON)
* 📤 **Outbox pattern** with batch publish and status tracking
* 🧩 **Idempotency** via `Idempotency-Key` with fingerprint & response replay
* 🔐 **JWT Auth**: access token + rotating refresh (HttpOnly cookie), owner vs `ADMIN`
* 💳 **Payments**: `PENDING → SUCCESS/FAILED` with a demo provider
* 📄 **Pagination** for payment listings
* 🧭 **Schema-first API**: OpenAPI 3 + generated interfaces, DTOs via MapStruct
* 🩺 **Actuator** (optional) for health/metrics

**API Docs:**

* 🧭 Swagger UI → [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* 📜 OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🏗️ Architecture

```
recurring-payments-api/
├── src/main/java/com/lukaszszumiec/recurring_payments_api/
│   ├── api/                  🎮 REST controllers (generated interfaces implemented)
│   │   └── mapper/           🔁 MapStruct mappers (Payment, Subscription)
│   ├── application/          ⚙️ Use-cases (services), DTOs
│   ├── config/               🔧 Properties, security config
│   ├── domain/               🏛️ Entities (User, Subscription, Payment, OutboxEvent, RefreshToken, IdempotencyKey)
│   │   └── port/             🔌 Repository ports
│   └── infrastructure/       🧱 Adapters: JPA repos, schedulers, security, payment provider, publisher
├── src/main/resources/
│   ├── db/migration/         🗄️ Flyway SQL migrations
│   └── openapi.yaml          📘 API contract (source of truth)
└── pom.xml                   🧩 Build & generators
```

---

## 🧪 Quick Start (Docker)

### 1) 🐘 Start PostgreSQL

```bash
docker run --name recurring-pg -e POSTGRES_PASSWORD=recurring \
  -e POSTGRES_USER=recurring -e POSTGRES_DB=recurring \
  -p 5432:5432 -d postgres:16
```

### 2) 🔑 Set environment

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/recurring
export SPRING_DATASOURCE_USERNAME=recurring
export SPRING_DATASOURCE_PASSWORD=recurring
# optional:
# export SERVER_PORT=8080
```

### 3) 🏃 Build & run

```bash
./mvnw clean package
java -jar target/recurring-payments-api-0.0.1-SNAPSHOT.jar
```

### 4) 🔎 Explore API

* 🧭 Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* 📜 OpenAPI: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 👤 Seed user (DEV)

* 📧 Email: `admin@local`
* 🔑 Password: `password`

Use for initial login and testing flows.

---

## 🔐 Auth Flow

1. 👋 **Login** → receive **access token** (JSON body) and **refresh token** (HttpOnly cookie).
2. 🛡️ Use access token as `Authorization: Bearer <token>` for protected endpoints.
3. ♻️ **Refresh** → `POST /api/auth/refresh` issues a new access token **and rotates** refresh.
4. 🚪 **Logout** → revokes refresh and clears the cookie.
5. 👥 Roles: `USER` (owner-scoped) and `ADMIN` (full access).

---

## 📚 Endpoints (selected)

### 🔑 Auth

* `POST /api/auth/login` – access token (body), refresh token (cookie)
* `POST /api/auth/refresh` – rotate refresh, new access token
* `POST /api/auth/logout` – revoke refresh
* `GET  /api/auth/me` – current principal

### 🧾 Subscriptions

* `GET  /api/subscriptions` – list (owner sees own; `ADMIN` sees all)
* `GET  /api/subscriptions/{id}` – details (owner or `ADMIN`)
* `POST /api/subscriptions` – create (**supports `Idempotency-Key`**)
* `POST /api/subscriptions/{id}/process` – run billing now (**supports `Idempotency-Key`**)

### 💳 Payments

* `GET  /api/users/{userId}/payments?page=&size=` – paginated payments (owner or `ADMIN`)

---

## ⏱️ Scheduling

* 🧮 **Billing scheduler**: finds subscriptions due “today” and creates payments.
* 📤 **Outbox publisher**: scans `payment_events_outbox` for `PENDING`, publishes in batches, marks `SENT`/`FAILED`.

Configure via `application.yml`:

```yaml
billing:
  charge-cron: "0 * * * * *"
outbox:
  publish-cron: "*/15 * * * * *"
  batch-size: 100
```

---

## 🧩 Idempotency

For write operations:

* Send `Idempotency-Key: <your-key>`
* Server stores the key, a request **fingerprint** (method + path + normalized body), and the original response.
* 🔁 Same key + same fingerprint → response replay.
* ⚠️ Same key + different fingerprint → `409 Conflict`.

---

## 🧰 Example cURL

### 1) 🔓 Login

```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "email=admin@local&password=password"
```

### 2) ➕ Create subscription (idempotent)

```bash
ACCESS="..."; IDEM="sub-create-123"
curl -s http://localhost:8080/api/subscriptions \
  -H "Authorization: Bearer $ACCESS" \
  -H "Idempotency-Key: $IDEM" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"price":"19.99","billingDayOfMonth":15}'
```

### 3) 📄 List payments (pagination)

```bash
curl -s "http://localhost:8080/api/users/1/payments?page=0&size=20" \
  -H "Authorization: Bearer $ACCESS"
```

### 4) ♻️ Refresh access token

```bash
curl -i -X POST http://localhost:8080/api/auth/refresh
```

### 5) 🚪 Logout

```bash
curl -i -X POST http://localhost:8080/api/auth/logout
```

---

## 🧩 Configuration (snippets)

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    locations: classpath:db/migration

jwt:
  secret: superSekretnyKluczJWT123456789012345678901234567890
  expiration: 3600
  refresh-expiration: 1209600
```

---

## 🧪 Testing

* ✅ `spring-boot-starter-test`
* 🧪 Recommended: **Testcontainers** for PostgreSQL integration tests

Suggested coverage:

* 📆 Subscription date rules
* 🔄 Payment status transitions & `nextChargeDate`
* 📤 Outbox state changes (`PENDING → SENT/FAILED`)
* 🔐 Access control (owner vs `ADMIN`)
* 🧩 Idempotency replay & conflicts

---

## 🗺️ Roadmap (nice-to-have)

* 💳 Real PSP integration (e.g., Stripe) behind `PaymentProvider`
* 🧾 Webhook signatures & retries
* 📈 Metrics/Tracing (Micrometer/OTel)
* 📦 DLQ for outbox after N failures
* 🗂️ Soft delete & audit log

---

## 🔗 API Documentation

* 🧭 Swagger UI → [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* 📜 OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

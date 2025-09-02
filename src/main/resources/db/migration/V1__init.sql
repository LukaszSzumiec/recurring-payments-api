CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL
);

CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    price NUMERIC(12,2) NOT NULL,
    next_charge_date DATE NOT NULL,
    billing_day_of_month INT NOT NULL
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions(id),
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Idempotency (unikalne płatności per subskrypcja + miesiąc)
CREATE UNIQUE INDEX uk_payment_unique_month
ON payments (subscription_id, date_trunc('month', created_at));

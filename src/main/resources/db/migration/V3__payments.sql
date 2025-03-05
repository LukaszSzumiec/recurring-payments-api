CREATE TABLE payments (
    id UUID PRIMARY KEY,
    subscription_id UUID NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
)

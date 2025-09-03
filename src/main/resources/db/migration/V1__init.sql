-- Users
create table if not exists users (
  id           bigserial primary key,
  email        varchar(255) not null unique,
  full_name    varchar(255) not null
);

-- Subscriptions
create table if not exists subscriptions (
  id                    bigserial primary key,
  user_id               bigint not null references users(id) on delete cascade,
  price                 numeric(12,2) not null,
  billing_day_of_month  int not null,
  next_charge_date      date not null,
  -- NOT NULL jest już na kolumnie, więc dodatkowy constraint niepotrzebny
  constraint chk_billing_day_bounds check (billing_day_of_month between 1 and 28)
);

-- Payments
create table if not exists payments (
  id               bigserial primary key,
  subscription_id  bigint not null references subscriptions(id) on delete cascade,
  amount           numeric(12,2) not null,
  status           varchar(16) not null,
  created_at       timestamp not null default now(),
  constraint chk_payment_status check (status in ('PENDING','SUCCESS','FAILED'))
);

-- Helpful indexes
create index if not exists idx_subscriptions_user on subscriptions(user_id);
create index if not exists idx_subscriptions_next_date on subscriptions(next_charge_date);

create index if not exists idx_payments_subscription on payments(subscription_id);
create index if not exists idx_payments_created_at on payments(created_at);

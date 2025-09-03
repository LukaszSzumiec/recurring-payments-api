create table if not exists payment_events_outbox (
    id bigserial primary key,
    aggregate_type varchar(64) not null,
    aggregate_id bigint not null,
    payload jsonb not null,
    status varchar(16) not null default 'PENDING',
    attempts int not null default 0,
    created_at timestamp not null default now(),
    last_attempt_at timestamp
);

create index if not exists idx_outbox_status on payment_events_outbox(status);

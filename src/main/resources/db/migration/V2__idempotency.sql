create table if not exists idempotency_keys (
    id bigserial primary key,
    idem_key varchar(128) not null unique,
    method varchar(10) not null,
    path varchar(255) not null,
    fingerprint varchar(128) not null,
    response_status int not null,
    response_body text,
    user_id bigint,
    created_at timestamp not null default now()
);

create index if not exists idx_idem_key on idempotency_keys(idem_key);

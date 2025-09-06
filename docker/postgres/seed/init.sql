-- DEV seed: user = admin@local / password (plaintext dla DelegatingPasswordEncoder)
insert into users (email, full_name, password)
values ('admin@local', 'Admin User', '$2a$10$11zsLs7WaKcVQPEg5720CeBKK0dk46htDH4TPKXbPXEyCHqMvZrnO')
on conflict (email) do nothing;

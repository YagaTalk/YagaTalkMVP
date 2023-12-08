ALTER TABLE assistant
    ADD status       varchar                  not null default 'ACTIVE',
    ADD updated_time timestamp with time zone not null default CURRENT_TIMESTAMP;
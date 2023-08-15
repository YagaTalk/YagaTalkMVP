ALTER TABLE context
    ADD created_time timestamp with time zone not null DEFAULT current_timestamp;

ALTER TABLE context
    ADD name text not null DEFAULT 'test_context';
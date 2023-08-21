ALTER TABLE chat_session
    ADD created_time timestamp with time zone not null DEFAULT current_timestamp;
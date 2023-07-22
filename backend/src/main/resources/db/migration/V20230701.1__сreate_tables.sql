CREATE TABLE context(
    id uuid PRIMARY KEY,
    content text not null
);

CREATE TABLE chat_session(
    id uuid PRIMARY KEY,
    context_id uuid not null REFERENCES context(id)
);

CREATE TABLE message (
    id uuid PRIMARY KEY,
    chat_session_id uuid not null REFERENCES chat_session(id),
    role varchar not null,
    created_time timestamp with time zone not null,
    content varchar not null
);


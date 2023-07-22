CREATE TABLE task_queue (
                            id uuid primary key,
                            task_type text not null,
                            task jsonb not null,
                            state varchar not null,
                            submitted_at timestamp with time zone not null
);
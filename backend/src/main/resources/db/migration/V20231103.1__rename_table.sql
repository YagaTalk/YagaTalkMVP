ALTER TABLE context RENAME TO assistant;

ALTER TABLE chat_session RENAME COLUMN context_id TO assistant_id;

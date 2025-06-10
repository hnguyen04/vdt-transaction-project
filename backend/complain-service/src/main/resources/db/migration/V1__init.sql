CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE complains (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL;
    content TEXT NOT NULL,
    resolver_id UUID,
    status VARCHAR(20) NOT NULL,
    resolving_note TEXT,
    time_submit TIMESTAMP NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
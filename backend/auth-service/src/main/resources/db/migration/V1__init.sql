CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id UUID PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    code VARCHAR(8) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    cmnd VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    code VARCHAR(8) NOT NULL UNIQUE,
    commit_time TIMESTAMP NOT NULL,
    amount NUMERIC CHECK (amount > 0),
    status VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    source_account VARCHAR(255),
    destination_account VARCHAR(255),
    bank_code VARCHAR(50),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    is_deleted BOOLEAN DEFAULT FALSE
);

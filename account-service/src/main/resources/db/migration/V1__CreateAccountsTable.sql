CREATE SCHEMA IF NOT EXISTS account_service;

CREATE TABLE
    account_service.accounts (
        id UUID PRIMARY KEY,
        user_id UUID NOT NULL,
        stripe_account_id VARCHAR(50),
        account_type VARCHAR(50) NOT NULL,
        balance DECIMAL(19, 4) NOT NULL,
        currency VARCHAR(3) NOT NULL,
        status VARCHAR(20) NOT NULL,
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_accounts_user_id ON accounts (user_id);
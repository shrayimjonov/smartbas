CREATE TABLE merchant_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL REFERENCES merchants(id),
    branch_code VARCHAR(64) NOT NULL,
    account_number VARCHAR(64) NOT NULL,
    currency VARCHAR(16) NOT NULL,
    abs_client_id VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_merchant_accounts_merchant_id ON merchant_accounts (merchant_id);
CREATE INDEX idx_merchant_accounts_account_currency ON merchant_accounts (account_number, currency);

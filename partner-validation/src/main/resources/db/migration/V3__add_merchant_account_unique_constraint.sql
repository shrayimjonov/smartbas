ALTER TABLE merchant_accounts
    ADD CONSTRAINT uk_merchant_accounts_merchant_account_currency
        UNIQUE (merchant_id, account_number, currency);

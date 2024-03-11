CREATE TABLE balance (
    id                  uuid                PRIMARY KEY,
    created_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    account_id          uuid                REFERENCES account(id),
    available_amount    BIGINT,
    currency_code       VARCHAR(3),
    UNIQUE (account_id, currency_code)
);

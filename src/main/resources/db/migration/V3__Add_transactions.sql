CREATE TABLE transaction (
    id                  uuid                PRIMARY KEY,
    created_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    account_id          uuid                REFERENCES account(id),
    amount              BIGINT,
    direction           VARCHAR(3)          NOT NULL,
    description         VARCHAR             NOT NULL
);

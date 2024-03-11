CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account (
	id                  uuid                PRIMARY KEY,
	created_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
	updated_at          TIMESTAMPTZ         NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    customer_id         uuid                NOT NULL,
    country             VARCHAR(255)        NOT NULL
);

CREATE TABLE oauth2user_provider
(
    id            SERIAL,
    provider_id   VARCHAR(255) NOT NULL,
    provider_type VARCHAR(255) NOT NULL,
    user_id       BIGINT       NOT NULL,
    provider_data JSONB,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    created_by_id BIGINT,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_by_id BIGINT,

    CONSTRAINT pk_oauth2_user_provider PRIMARY KEY (id)
);

ALTER TABLE oauth2user_provider
    ADD CONSTRAINT FK_OAUTH2_USER_PROVIDER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT FK_OAUTH2_USER_PROVIDER_ON_CREATED_BY FOREIGN KEY (created_by_id) REFERENCES users (id),
    ADD CONSTRAINT FK_OAUTH2_USER_PROVIDER_ON_UPDATED_BY FOREIGN KEY (updated_by_id) REFERENCES users (id),
    ADD CONSTRAINT UQ_OAUTH2_USER_PROVIDER_PROVIDER_ID_PROVIDER_TYPE_USER_ID UNIQUE (provider_id, provider_type),
    ADD CONSTRAINT UQ_OAUTH2_USER_PROVIDER_USER_ID_PROVIDER_TYPE UNIQUE (user_id, provider_type);

ALTER TABLE users
    ALTER COLUMN password DROP NOT NULL,
    DROP CONSTRAINT IF EXISTS ck_users_password_not_empty,
    ALTER COLUMN email DROP NOT NULL,
    DROP CONSTRAINT IF EXISTS ck_users_email_not_empty;
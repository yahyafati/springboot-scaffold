CREATE TABLE permissions
(
    id            SERIAL       NOT NULL,
    name          VARCHAR(255) NOT NULL,

    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    created_by_id BIGINT,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_by_id BIGINT,

    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    permission_id BIGINT NOT NULL,
    role_id       BIGINT NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_id, role_id)
);

CREATE TABLE roles
(
    id            SERIAL       NOT NULL,
    name          VARCHAR(255) NOT NULL,

    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    created_by_id BIGINT,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_by_id BIGINT,

    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_permissions
(
    type          VARCHAR(255),
    auth_user_id  BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    CONSTRAINT pk_user_permissions PRIMARY KEY (auth_user_id, permission_id)
);

CREATE TABLE users
(
    id            SERIAL       NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    created_by_id BIGINT,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_by_id BIGINT,
    username      VARCHAR(50)  NOT NULL,
    password      VARCHAR(255) NOT NULL,
    email         VARCHAR(100) NOT NULL,
    enabled       BOOLEAN      NOT NULL       DEFAULT FALSE,
    role_id       BIGINT,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username),
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_permissions
    ADD CONSTRAINT FK_USER_PERMISSIONS_ON_AUTH_USER FOREIGN KEY (auth_user_id) REFERENCES users (id);

ALTER TABLE user_permissions
    ADD CONSTRAINT FK_USER_PERMISSIONS_ON_PERMISSION FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users
    ADD CONSTRAINT ck_users_username_not_empty CHECK ( LENGTH(username) > 0 ),
    ADD CONSTRAINT ck_users_password_not_empty CHECK ( LENGTH(password) > 0 ),
    ADD CONSTRAINT ck_users_email_not_empty CHECK ( LENGTH(email) > 0 );

CREATE SCHEMA IF NOT EXISTS user_service;

CREATE TABLE
    user_service.users (
        id UUID PRIMARY KEY,
        stripeCustomerId VARCHAR(255),
        email VARCHAR(255) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        first_name VARCHAR(100),
        last_name VARCHAR(100),
        date_of_birth DATE,
        phone_number VARCHAR(20),
        status VARCHAR(20) NOT NULL,
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE
    user_service.blacklisted_tokens (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        token VARCHAR(512) UNIQUE NOT NULL,
        blacklisted_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE
    user_service.roles (
        role_id UUID NOT NULL DEFAULT gen_random_uuid (),
        user_id UUID NOT NULL REFERENCES user_service.users (id),
        role_name VARCHAR(50) NOT NULL,
        PRIMARY KEY (user_id, role_name)
    );

CREATE TABLE
    user_service.user_profiles (
        user_id UUID PRIMARY KEY REFERENCES user_service.users (id),
        address_line1 VARCHAR(255),
        address_line2 VARCHAR(255),
        city VARCHAR(100),
        state VARCHAR(100),
        country VARCHAR(100),
        postal_code VARCHAR(20),
        kyc_status VARCHAR(20),
        created_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP
        WITH
            TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE
    IF NOT EXISTS user_service.know_your_customer (
        user_id UUID PRIMARY KEY REFERENCES user_service.users (id),
        id_type VARCHAR(255),
        id_number VARCHAR(255),
        id_expiry_date TIMESTAMP,
        address_proof_type VARCHAR(255),
        address_proof_document BYTEA,
        id_proof_document BYTEA
    );

CREATE TABLE
    user_service.payment_methods (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        method_type VARCHAR(255) NOT NULL,
        provider VARCHAR(255) NOT NULL,
        token_id VARCHAR(255) NOT NULL,
        last4 VARCHAR(4) NOT NULL,
        user_id UUID NOT NULL REFERENCES user_service.users (id)
    );

-- Insert a user into the users table
INSERT INTO
    user_service.users (
        id,
        email,
        password_hash,
        first_name,
        last_name,
        date_of_birth,
        phone_number,
        status
    )
VALUES
    (
        gen_random_uuid (),
        'admin@example.com',
        '$2a$12$tJdS1dsihLXGulvTAweukuvNUJvY3alnJFaFQKGbOIyvzrMW.Oi56',
        'Admin',
        'User',
        '1985-01-01',
        '0987654321',
        'ACTIVE'
    );

-- Retrieve the user's UUID
WITH
    user_uuid AS (
        SELECT
            id
        FROM
            user_service.users
        WHERE
            email = 'admin@example.com'
    )
    -- Insert the role for the user
INSERT INTO
    user_service.roles (user_id, role_name)
SELECT
    id,
    'ROLE_ADMIN'
FROM
    user_uuid;

-- Insert user profile for ROLE_ADMIN user
WITH
    user_uuid AS (
        SELECT
            id
        FROM
            user_service.users
        WHERE
            email = 'admin@example.com'
    )
INSERT INTO
    user_service.user_profiles (
        user_id,
        address_line1,
        address_line2,
        city,
        state,
        country,
        postal_code,
        kyc_status
    )
SELECT
    id,
    '456 Another St',
    'Apt 2',
    'AdminCity',
    'AdminState',
    'AdminCountry',
    '67890',
    'PENDING'
FROM
    user_uuid;

-- Insert a user into the users table
INSERT INTO
    user_service.users (
        id,
        email,
        password_hash,
        first_name,
        last_name,
        date_of_birth,
        phone_number,
        status
    )
VALUES
    (
        gen_random_uuid (),
        'superadmin@example.com',
        '$2a$12$tJdS1dsihLXGulvTAweukuvNUJvY3alnJFaFQKGbOIyvzrMW.Oi56',
        'Super',
        'Admin',
        '1980-01-01',
        '1234567890',
        'ACTIVE'
    );

-- Retrieve the user's UUID
WITH
    user_uuid AS (
        SELECT
            id
        FROM
            user_service.users
        WHERE
            email = 'superadmin@example.com'
    )
    -- Insert the role for the user
INSERT INTO
    user_service.roles (user_id, role_name)
SELECT
    id,
    'ROLE_SUPERADMIN'
FROM
    user_uuid;

-- Insert user profile for ROLE_SUPERADMIN user
WITH
    user_uuid AS (
        SELECT
            id
        FROM
            user_service.users
        WHERE
            email = 'superadmin@example.com'
    )
INSERT INTO
    user_service.user_profiles (
        user_id,
        address_line1,
        address_line2,
        city,
        state,
        country,
        postal_code,
        kyc_status
    )
SELECT
    id,
    '123 Main St',
    'Apt 1',
    'SuperCity',
    'SuperState',
    'SuperCountry',
    '12345',
    'APPROVED'
FROM
    user_uuid;
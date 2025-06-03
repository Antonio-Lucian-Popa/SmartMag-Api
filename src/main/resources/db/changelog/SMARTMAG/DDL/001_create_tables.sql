--liquibase formatted sql

--changeset admin:create-extension-uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset admin:create-table-company
CREATE TABLE company (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    cui VARCHAR(50) UNIQUE NOT NULL
);

--changeset admin:create-table-store
CREATE TABLE store (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    address TEXT,
    company_id UUID NOT NULL REFERENCES company(id)
);

--changeset admin:create-table-user
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    keycloak_id UUID NOT NULL UNIQUE,
    company_id UUID NOT NULL REFERENCES company(id)
);

--changeset admin:create-table-user-store
CREATE TABLE user_store (
    user_id UUID NOT NULL REFERENCES users(id),
    store_id UUID NOT NULL REFERENCES store(id),
    PRIMARY KEY (user_id, store_id)
);

--changeset admin:create-table-product
CREATE TABLE product (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(100) UNIQUE NOT NULL,
    unit VARCHAR(50),
    is_sgr BOOLEAN DEFAULT FALSE,
    sgr_value NUMERIC(10, 2) DEFAULT 0,
    company_id UUID NOT NULL REFERENCES company(id)
);

--changeset admin:create-table-product-stock
CREATE TABLE product_stock (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES product(id),
    store_id UUID NOT NULL REFERENCES store(id),
    quantity INTEGER NOT NULL DEFAULT 0
);

--changeset admin:create-table-stock-entry-log
CREATE TABLE stock_entry_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES product(id),
    store_id UUID NOT NULL REFERENCES store(id),
    user_id UUID REFERENCES users(id),
    quantity INTEGER NOT NULL,
    type VARCHAR(20) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset admin:create-table-shift-schedule
CREATE TABLE shift_schedule (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    store_id UUID NOT NULL REFERENCES store(id),
    date DATE NOT NULL,
    shift_type VARCHAR(20) NOT NULL,
    is_confirmed BOOLEAN DEFAULT FALSE
);

--changeset admin:create-table-shift-swap-request
CREATE TABLE shift_swap_request (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    from_user_id UUID NOT NULL REFERENCES users(id),
    to_user_id UUID NOT NULL REFERENCES users(id),
    store_id UUID NOT NULL REFERENCES store(id),
    date DATE NOT NULL,
    shift_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

--changeset admin:create-table-time-off-request
CREATE TABLE time_off_request (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

--changeset admin:create-table-attendance-log
CREATE TABLE attendance_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    store_id UUID NOT NULL REFERENCES store(id),
    date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20) NOT NULL DEFAULT 'PRESENT',
    note TEXT
);

--changeset admin:create-table-notification
CREATE TABLE notification (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    message TEXT NOT NULL,
    type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--changeset admin:create-table-audit-log
CREATE TABLE audit_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    action VARCHAR(255) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    entity_id UUID,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

--changeset admin:create-table-shift-swap-history
CREATE TABLE shift_swap_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    old_user_id UUID NOT NULL REFERENCES users(id),
    new_user_id UUID NOT NULL REFERENCES users(id),
    store_id UUID NOT NULL REFERENCES store(id),
    date DATE NOT NULL,
    shift_type VARCHAR(20) NOT NULL,
    approved_by UUID REFERENCES users(id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--changeset admin:create-table-file-upload
CREATE TABLE file_upload (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    file_name VARCHAR(255),
    file_url TEXT NOT NULL,
    entity_type VARCHAR(50), -- ex: 'time_off_request', 'product'
    entity_id UUID,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

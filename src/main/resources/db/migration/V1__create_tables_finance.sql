CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    token_valid_since TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wallets (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES users(id) NOT NULL,
    balance DECIMAL(19,4) DEFAULT 0 NOT NULL
);

CREATE TYPE operations_types AS ENUM ('DEPOSIT', 'TRANSFER', 'REFUND');

CREATE TABLE transactions (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    wallet_sender_id UUID REFERENCES wallets(id),
    wallet_receiver_id REFERENCES wallets(id) NOT NULL,
    amount DECIMAL(19,4) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    operation_type operations_types NOT NULL
);
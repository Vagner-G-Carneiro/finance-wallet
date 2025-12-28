ALTER TABLE users
ADD COLUMN active BOOLEAN DEFAULT TRUE;

CREATE TYPE wallets_status_enum AS ENUM ('ACTIVE', 'FROZEN', 'CLOSED');
CREATE CAST (character varying AS wallets_status_enum) WITH INOUT AS IMPLICIT;

ALTER TABLE wallets
ADD COLUMN wallet_status wallets_status_enum DEFAULT 'ACTIVE';
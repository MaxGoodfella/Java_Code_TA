DROP TABLE IF EXISTS transactions, wallets;


CREATE TABLE IF NOT EXISTS wallets (
    wallet_id UUID PRIMARY KEY,
    balance DECIMAL(15, 2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    operation_type VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id) ON DELETE CASCADE
);
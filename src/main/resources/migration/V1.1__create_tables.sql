CREATE TABLE IF NOT EXISTS user_data
(
    email        VARCHAR PRIMARY KEY,
    name         VARCHAR NOT NULL,
    password     VARCHAR NOT NULL,
    created_at   TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_email_password_user ON user_data(email, password);


CREATE TABLE IF NOT EXISTS user_historical_transaction
(
    id          VARCHAR PRIMARY KEY,
    user_id     VARCHAR NOT NULL CONSTRAINT historical_transaction_user_data_email_fk REFERENCES user_data ON DELETE CASCADE,
    city        VARCHAR,
    lat         DOUBLE PRECISION,
    lng         DOUBLE PRECISION,
    created_at  TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_user_id_historical_transaction ON user_historical_transaction(user_id);

CREATE TABLE IF NOT EXISTS historical_restaurant
(
    id               VARCHAR PRIMARY KEY,
    transaction_id   VARCHAR NOT NULL CONSTRAINT historical_restaurant_historical_transaction_id_fk REFERENCES user_historical_transaction ON DELETE CASCADE,
    name             VARCHAR,
    address          VARCHAR,
    phone            VARCHAR,
    score            DOUBLE PRECISION
);

CREATE INDEX IF NOT EXISTS idx_transaction_id_historical_restaurant ON historical_restaurant(transaction_id);
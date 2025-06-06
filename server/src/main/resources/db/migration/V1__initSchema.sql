CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       location_id BIGINT UNIQUE
);

-- Insert sample users
INSERT INTO users (id, name, email, password_hash, location_id)
VALUES
    ('user-1', 'Alice Ahmed', 'alice@example.com', 'hashedpassword1', null),
    ('user-2', 'Bob Karim', 'bob@example.com', 'hashedpassword2', null);

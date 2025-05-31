CREATE TABLE user_location (
                               id SERIAL PRIMARY KEY,
                               address VARCHAR(100) NOT NULL,
                               thana VARCHAR(100),
                               po VARCHAR(100) NOT NULL,
                               city VARCHAR(100) NOT NULL,
                               postal_code VARCHAR(20) NOT NULL
);

CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       location_id INTEGER UNIQUE,
                       CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES user_location(id)
);

-- Insert sample locations
INSERT INTO user_location (address, thana, po, city, postal_code)
VALUES
    ('123 Main St', 'Dhanmondi', 'PO123', 'Dhaka', '1209'),
    ('456 Park Rd', 'Banani', 'PO456', 'Dhaka', '1213');

-- Insert sample users
INSERT INTO users (id, name, email, password_hash, location_id)
VALUES
    ('user-1', 'Alice Ahmed', 'alice@example.com', 'hashedpassword1', 1),
    ('user-2', 'Bob Karim', 'bob@example.com', 'hashedpassword2', 2);

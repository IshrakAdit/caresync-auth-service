
CREATE TYPE role_type AS ENUM ('DEFAULT', 'ADMIN');

CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role role_type NOT NULL,
                       location_id BIGINT UNIQUE
);

INSERT INTO users (id, name, email, password_hash, role, location_id)
VALUES
    ('user-1', 'Alice Ahmed', 'alice@example.com', 'hashedpassword1', 'ADMIN', null),
    ('user-2', 'Bob Karim', 'bob@example.com', 'hashedpassword2', 'DEFAULT', null),
    ('user-3', 'Charlie Rahman', 'charlie@example.com', 'hashedpassword3', 'DEFAULT', null),
    ('user-4', 'Dipa Sultana', 'dipa@example.com', 'hashedpassword4', 'DEFAULT', null),
    ('user-5', 'Emran Hossain', 'emran@example.com', 'hashedpassword5', 'DEFAULT', null),
    ('user-6', 'Farzana Khan', 'farzana@example.com', 'hashedpassword6', 'DEFAULT', null),
    ('user-7', 'Gias Uddin', 'gias@example.com', 'hashedpassword7', 'DEFAULT', null),
    ('user-8', 'Hasina Jahan', 'hasina@example.com', 'hashedpassword8', 'DEFAULT', null),
    ('user-9', 'Imran Ali', 'imran@example.com', 'hashedpassword9', 'DEFAULT', null),
    ('user-10', 'Jannat Akter', 'jannat@example.com', 'hashedpassword10', 'DEFAULT', null),
    ('user-11', 'Kamal Mia', 'kamal@example.com', 'hashedpassword11', 'DEFAULT', null),
    ('user-12', 'Lamia Ferdous', 'lamia@example.com', 'hashedpassword12', 'DEFAULT', null);


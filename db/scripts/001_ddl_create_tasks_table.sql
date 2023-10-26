CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       description TEXT,
                       title text,
                       created TIMESTAMP,
                       done BOOLEAN
);
CREATE TABLE IF NOT EXISTS users (
    id UUID NOT NULL,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    PRIMARY KEY (id)
);

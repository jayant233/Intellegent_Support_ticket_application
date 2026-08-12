-- Create database if it does not exist (uncomment if you want Spring to create DB, but usually MySQL requires DB to exist first for the URL)
-- CREATE DATABASE IF NOT EXISTS support_ticket_db;

CREATE TABLE IF NOT EXISTS tickets (
    ticket_id VARCHAR(50) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100),
    severity VARCHAR(50),
    priority VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    sla_hours INT
);

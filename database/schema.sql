-- ========================================================
-- Support Ticket Management System - Database Schema
-- ========================================================

CREATE DATABASE IF NOT EXISTS support_ticket_db;
USE support_ticket_db;

DROP TABLE IF EXISTS tickets;

CREATE TABLE tickets (
    ticket_id VARCHAR(50) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100),
    severity VARCHAR(50),
    priority VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    sla_hours INT
);

-- Seed Sample Data (Optional)
INSERT INTO tickets (ticket_id, customer_name, description, category, severity, priority, status, sla_hours) VALUES
('TKT1001', 'Alice Johnson', 'Cannot login to my account, password reset not working.', 'Login Issue', 'Medium', 'P3', 'Open', 8),
('TKT1002', 'Bob Williams', 'Production server down and service unavailable for all users.', 'Outage Issue', 'Critical', 'P1', 'In Progress', 1),
('TKT1003', 'Charlie Brown', 'Payment failed during checkout with credit card.', 'Payment Issue', 'High', 'P2', 'Open', 4),
('TKT1004', 'Diana Prince', 'UI button overlapping with text on dashboard page.', 'UI Bug', 'Low', 'P4', 'Resolved', 48);

-- Fitxer de dades per a tests (H2) - Sprint 3 (TEA4)
-- @author Gess Montalbán
-- Totes les contrasenyes estan xifrades amb BCrypt

-- admin / 1234
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'admin@sportspot.com', 'admin', '$2a$10$z3xqYQ8sZ49QshC212oJeeKBvzWovvncvMK8fEqzvIIAosrEieOQm', 'ADMIN');

-- joanet / 5678
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'joanet@sportspot.com', 'joanet', '$2a$10$fjqv1lT4UsLQxlk0LgPzxu/AGW4KHgvo.QeLWZ8KV9thi384u2/hO', 'CLIENT');


-- joan_99 / pass123
MERGE INTO usuaris (active, email, name, password, role) KEY(name) 
VALUES (true, 'joan@test.com', 'joan_99', '$2a$10$mC1B9.uR8M4p6Vz.E7E8O.uM8mO9mO9mO9mO9mO9mO9mO9mO9mO9', 'USER');

-- marta / marta2024
MERGE INTO usuaris (active, email, name, password, role) KEY(name) 
VALUES (true, 'marta@test.com', 'marta', '$2a$10$Xm5A8O7Y6R5T4E3W2Q1P0O.uM8mO9mO9mO9mO9mO9mO9mO9mO9mO9', 'USER');

-- admin_boss / admin123
MERGE INTO usuaris (active, email, name, password, role) KEY(name) 
VALUES (true, 'admin_boss@sportspot.com', 'admin_boss', '$2a$10$7R5T4E3W2Q1P0O9I8U7Y6O.uM8mO9mO9mO9mO9mO9mO9mO9mO9mO9', 'ADMIN');

-- Inserció de pistes (Courts) per tenir dades inicials
MERGE INTO pistes (name, type, price_per_hour, capacity,location) KEY(name) VALUES ('Pista Central', 'Pàdel', 24.0, 4, 'València');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista 2', 'Pàdel', 20.0, 12,'Calafell');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Coberta', 'Bàsquet', 35.0, 20, 'Girona');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Exterior 1', 'Tennis', 15.0,15, 'Barcelona');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Exterior 2', 'Futbol 5', 40.0,24, 'Sta. Coloma');

-- Opcional: Podríem afegir alguna reserva (Booking) de prova si volguéssim,
-- però normalment les reserves es creen dinàmicament durant els tests.
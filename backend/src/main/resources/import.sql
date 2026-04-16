-- Fem servir MERGE per a tots per evitar errors de duplicitat si el test reinicia el context
MERGE INTO usuaris (name, password, email, role, active) KEY(name) VALUES ('admin','1234','admin@sportspot.com','ADMIN',true);
MERGE INTO usuaris (name, password, email, role, active) KEY(name) VALUES ('joanet','5678','joanet@sportspot.com','CLIENT',true);
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'joan@test.com', 'joan_99', 'pass123', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'marta@test.com', 'marta', 'marta2024', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'pere@test.com', 'pere_pro', 'pere123', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (false, 'anna@test.com', 'anna_', 'anna789', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'laura@test.com', 'laura_fit', 'laura456', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'admin_boss@sportspot.com', 'admin_boss', 'admin123', 'ADMIN');

-- Inserció de pistes (Courts) per tenir dades inicials
MERGE INTO pistes (name, type, price_per_hour, capacity,location) KEY(name) VALUES ('Pista Central', 'Pàdel', 24.0, 4, 'València');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista 2', 'Pàdel', 20.0, 12,'Calafell');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Coberta', 'Bàsquet', 35.0, 20, 'Girona');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Exterior 1', 'Tennis', 15.0,15, 'Barcelona');
MERGE INTO pistes (name, type, price_per_hour, capacity, location) KEY(name) VALUES ('Pista Exterior 2', 'Futbol 5', 40.0,24, 'Sta. Coloma');

-- Opcional: Podríem afegir alguna reserva (Booking) de prova si volguéssim,
-- però normalment les reserves es creen dinàmicament durant els tests.
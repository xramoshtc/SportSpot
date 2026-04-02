-- Fem servir MERGE per a tots per evitar errors de duplicitat si el test reinicia el context
MERGE INTO usuaris (name, password, email, role, active) KEY(name) VALUES ('admin','1234','admin@sportspot.com','ADMIN',true);
MERGE INTO usuaris (name, password, email, role, active) KEY(name) VALUES ('joanet','5678','joanet@sportspot.com','CLIENT',true);
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'joan@test.com', 'joan_99', 'pass123', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'marta@test.com', 'marta', 'marta2024', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'pere@test.com', 'pere_pro', 'pere123', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (false, 'anna@test.com', 'anna_', 'anna789', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'laura@test.com', 'laura_fit', 'laura456', 'USER');
MERGE INTO usuaris (active, email, name, password, role) KEY(name) VALUES (true, 'admin_boss@sportspot.com', 'admin_boss', 'admin123', 'ADMIN');

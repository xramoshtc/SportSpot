/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
    Injectem dades inicials a la base de dades
 * Author:  Gess
 * Created: 20 mar 2026
 */

MERGE INTO usuaris(name, password, email, role)
KEY(name)
VALUES ('admin','1234','admin@sportspot.com','ADMIN');
MERGE INTO usuaris(name, password, email, role)
KEY(name)
VALUES ('joanet','5678','joanet@sportspot.com','CLIENT');

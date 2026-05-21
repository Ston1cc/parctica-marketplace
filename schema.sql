-- Marketplace App — Database Schema
-- Run this once to create the database and tables.
-- MySQL credentials used in DatabaseConnection.java: root / dani

CREATE DATABASE IF NOT EXISTS marketplace_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE marketplace_db;

-- ─── Vanzatori ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vanzatori (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nume    VARCHAR(100) NOT NULL,
    email   VARCHAR(150) NOT NULL UNIQUE,
    telefon VARCHAR(20),
    adresa  VARCHAR(255),
    activ   BOOLEAN DEFAULT TRUE
);

-- ─── Produse ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS produse (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nume             VARCHAR(150) NOT NULL,
    descriere        TEXT,
    pret             DECIMAL(10,2) NOT NULL,
    stoc             INT DEFAULT 0,
    tip              ENUM('FIZIC','DIGITAL') NOT NULL,
    greutate_kg      DECIMAL(8,3),
    dimensiuni       VARCHAR(100),
    link_descarcare  VARCHAR(500),
    format_fisier    VARCHAR(50),
    vanzator_id      INT NOT NULL,
    activ            BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (vanzator_id) REFERENCES vanzatori(id)
);

-- ─── Comenzi ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS comenzi (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    produs_id        INT NOT NULL,
    vanzator_id      INT NOT NULL,
    cantitate        INT NOT NULL,
    pret_unitar      DECIMAL(10,2) NOT NULL,
    total_pret       DECIMAL(10,2) NOT NULL,
    status           ENUM('IN_PROCESARE','CONFIRMATA','EXPEDIATA','LIVRATA','ANULATA')
                         NOT NULL DEFAULT 'IN_PROCESARE',
    nume_cumparator  VARCHAR(100),
    email_cumparator VARCHAR(150),
    adresa_livrare   VARCHAR(255),
    data_comanda     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (produs_id)   REFERENCES produse(id),
    FOREIGN KEY (vanzator_id) REFERENCES vanzatori(id)
);

-- ─── Sample data ──────────────────────────────────────────
INSERT IGNORE INTO vanzatori (id, nume, email, telefon, adresa) VALUES
(1, 'Ion Popescu',  'ion@example.com',  '0722111111', 'Bucuresti, Str. Victoriei 1'),
(2, 'Maria Ionescu','maria@example.com','0733222222', 'Cluj-Napoca, Str. Avram Iancu 5');

INSERT IGNORE INTO produse (id, nume, descriere, pret, stoc, tip, greutate_kg, dimensiuni, vanzator_id) VALUES
(1, 'Laptop Gaming', 'Laptop pentru gaming', 3500.00, 10, 'FIZIC', 2.5, '35x24x2 cm', 1),
(2, 'Mouse Wireless', 'Mouse ergonomic', 150.00, 50, 'FIZIC', 0.1, '12x6x4 cm', 1);

INSERT IGNORE INTO produse (id, nume, descriere, pret, stoc, tip, link_descarcare, format_fisier, vanzator_id) VALUES
(3, 'Curs Java Pro', 'Curs complet Java', 299.00, 999, 'DIGITAL', 'https://example.com/curs-java', 'PDF', 2);

INSERT IGNORE INTO comenzi (produs_id, vanzator_id, cantitate, pret_unitar, total_pret, status, nume_cumparator, email_cumparator, adresa_livrare) VALUES
(1, 1, 1, 3500.00, 3500.00, 'CONFIRMATA',  'Andrei Mihai', 'andrei@test.com', 'Timisoara, Str. Florilor 3'),
(2, 1, 2, 150.00,  300.00,  'IN_PROCESARE','Elena Vasile',  'elena@test.com',  'Iasi, Bld. Copou 10');

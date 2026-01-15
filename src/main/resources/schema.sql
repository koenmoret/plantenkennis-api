-- =========================================
-- 0) Optioneel: drop in juiste volgorde
-- =========================================
DROP TABLE IF EXISTS plantkenmerk CASCADE;
DROP TABLE IF EXISTS foto CASCADE;
DROP TABLE IF EXISTS synoniem CASCADE;
DROP TABLE IF EXISTS favoriet CASCADE;
DROP TABLE IF EXISTS spelsessie CASCADE;
DROP TABLE IF EXISTS kenmerk CASCADE;
DROP TABLE IF EXISTS plant_soort CASCADE;
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS speler CASCADE;
DROP TABLE IF EXISTS gebruiker CASCADE;

-- =========================================
-- 1) Gebruikers + overerving (table-per-subclass)
-- =========================================
CREATE TABLE gebruiker (
                           id              BIGSERIAL PRIMARY KEY,
                           naam            VARCHAR(120) NOT NULL,
                           email           VARCHAR(255) NOT NULL UNIQUE,
                           wachtwoord_hash VARCHAR(255) NOT NULL,
                           created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Subclass: Admin (PK = FK naar gebruiker)
CREATE TABLE admin (
                       id BIGINT PRIMARY KEY,
                       CONSTRAINT fk_admin_gebruiker
                           FOREIGN KEY (id) REFERENCES gebruiker(id)
                               ON DELETE CASCADE
);

-- Subclass: Speler (PK = FK naar gebruiker)
CREATE TABLE speler (
                        id BIGINT PRIMARY KEY,
                        CONSTRAINT fk_speler_gebruiker
                            FOREIGN KEY (id) REFERENCES gebruiker(id)
                                ON DELETE CASCADE
);

-- =========================================
-- 2) PlantSoort
-- =========================================
CREATE TABLE plant_soort (
                             id                      BIGSERIAL PRIMARY KEY,
                             wetenschappelijke_naam  VARCHAR(255) NOT NULL,
                             nederlandse_naam        VARCHAR(255) NOT NULL,
                             familie                 VARCHAR(255),
                             beschrijving            VARCHAR(2000),
                             bloeiperiode_start      INTEGER,
                             bloeiperiode_einde      INTEGER,
                             giftig                  BOOLEAN DEFAULT FALSE,
                             inheems                 BOOLEAN DEFAULT FALSE,
                             onderhoudsniveau        VARCHAR(80),
                             slug                    VARCHAR(255),
                             updated_at              TIMESTAMP
);

-- (optioneel maar handig)
CREATE UNIQUE INDEX ux_plant_soort_wetenschappelijke_naam
    ON plant_soort (wetenschappelijke_naam);

-- =========================================
-- 3) Foto (compositie bij PlantSoort)
-- =========================================
CREATE TABLE foto (
                      id          BIGSERIAL PRIMARY KEY,
                      plant_soort_id BIGINT NOT NULL,
                      url         VARCHAR(1000) NOT NULL,
                      fotograaf   VARCHAR(255),
                      licentie    VARCHAR(255),
                      alt_tekst   VARCHAR(255),
                      hoofdfoto   BOOLEAN DEFAULT FALSE,
                      bron        VARCHAR(255),
                      CONSTRAINT fk_foto_plant_soort
                          FOREIGN KEY (plant_soort_id) REFERENCES plant_soort(id)
                              ON DELETE CASCADE
);

-- =========================================
-- 4) Synoniem (compositie bij PlantSoort)
-- =========================================
CREATE TABLE synoniem (
                          id            BIGSERIAL PRIMARY KEY,
                          plant_soort_id BIGINT NOT NULL,
                          naam          VARCHAR(255) NOT NULL,
                          CONSTRAINT fk_synoniem_plant_soort
                              FOREIGN KEY (plant_soort_id) REFERENCES plant_soort(id)
                                  ON DELETE CASCADE
);

-- =========================================
-- 5) Kenmerk
-- =========================================
CREATE TABLE kenmerk (
                         id      BIGSERIAL PRIMARY KEY,
                         type    VARCHAR(255) NOT NULL,
                         waarde  VARCHAR(255) NOT NULL
);

-- Uniek kenmerk op (type, waarde) om dubbele records te voorkomen
CREATE UNIQUE INDEX ux_kenmerk_type_waarde
    ON kenmerk (LOWER(type), LOWER(waarde));

-- =========================================
-- 6) PlantKenmerk (koppelentiteit voor N-M)
-- PlantSoort 1..* -> PlantKenmerk
-- Kenmerk   1..* -> PlantKenmerk
-- =========================================
CREATE TABLE plantkenmerk (
                              id            BIGSERIAL PRIMARY KEY,
                              plant_soort_id BIGINT NOT NULL,
                              kenmerk_id    BIGINT NOT NULL,

                              CONSTRAINT fk_plantkenmerk_plant_soort
                                  FOREIGN KEY (plant_soort_id) REFERENCES plant_soort(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_plantkenmerk_kenmerk
                                  FOREIGN KEY (kenmerk_id) REFERENCES kenmerk(id)
                                      ON DELETE RESTRICT
);

-- Voorkom dubbele koppelingen (zelfde kenmerk 2x aan dezelfde plant)
CREATE UNIQUE INDEX ux_plantkenmerk_unique_pair
    ON plantkenmerk (plant_soort_id, kenmerk_id);

-- =========================================
-- 7) Spelsessie (compositie bij Gebruiker)
-- =========================================
CREATE TABLE spelsessie (
                            id              BIGSERIAL PRIMARY KEY,
                            gebruiker_id    BIGINT NOT NULL,
                            modus           VARCHAR(80) NOT NULL,
                            level           INTEGER,
                            score           INTEGER,
                            duur_sec        INTEGER,
                            aantal_correct  INTEGER,
                            aantal_pogingen INTEGER,
                            gespeeld_op     TIMESTAMP NOT NULL DEFAULT NOW(),

                            CONSTRAINT fk_spelsessie_gebruiker
                                FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id)
                                    ON DELETE CASCADE
);

-- =========================================
-- 8) Favoriet (compositie bij Gebruiker, verwijst naar PlantSoort)
-- =========================================
CREATE TABLE favoriet (
                          id            BIGSERIAL PRIMARY KEY,
                          gebruiker_id  BIGINT NOT NULL,
                          plant_soort_id BIGINT NOT NULL,
                          aangemaakt_op TIMESTAMP NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_favoriet_gebruiker
                              FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_favoriet_plant_soort
                              FOREIGN KEY (plant_soort_id) REFERENCES plant_soort(id)
                                  ON DELETE RESTRICT
);

-- Voorkom dubbele favorieten per gebruiker
CREATE UNIQUE INDEX ux_favoriet_unique_pair
    ON favoriet (gebruiker_id, plant_soort_id);

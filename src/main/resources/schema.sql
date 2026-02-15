-- =========================================
-- schema.sql - Plantenkennis API (B: meervoud)
-- =========================================

-- =========================================
-- 0) Drop in juiste volgorde
-- =========================================
DROP TABLE IF EXISTS plantkenmerk CASCADE;
DROP TABLE IF EXISTS foto CASCADE;
DROP TABLE IF EXISTS synoniem CASCADE;
DROP TABLE IF EXISTS favorieten CASCADE;
DROP TABLE IF EXISTS spelsessies CASCADE;
DROP TABLE IF EXISTS kenmerken CASCADE;
DROP TABLE IF EXISTS plant_soorten CASCADE;
DROP TABLE IF EXISTS gebruikers CASCADE;

-- =========================================
-- 1) Gebruikers (geen overerving in fase 2)
-- =========================================
CREATE TABLE gebruikers (
                            id                BIGSERIAL PRIMARY KEY,

    -- ✅ Keycloak stable identifier (sub)
                            keycloak_subject  VARCHAR(100) NOT NULL UNIQUE,

                            naam              VARCHAR(120) NOT NULL,
                            email             VARCHAR(190) NOT NULL UNIQUE,

                            wachtwoord_hash   VARCHAR(255),
                            created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX ix_gebruikers_keycloak_subject
    ON gebruikers (keycloak_subject);


-- =========================================
-- 2) PlantSoorten
-- =========================================
CREATE TABLE plant_soorten (
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
CREATE UNIQUE INDEX ux_plant_soorten_wetenschappelijke_naam
    ON plant_soorten (wetenschappelijke_naam);

-- =========================================
-- 3) Foto (compositie bij PlantSoort)
-- =========================================
CREATE TABLE foto (
                      id                BIGSERIAL PRIMARY KEY,
                      plant_soort_id    BIGINT NOT NULL,
                      url               VARCHAR(500) NOT NULL,

    -- Opslag info filesystem
                      storage_path      VARCHAR(500) NOT NULL,
                      original_filename VARCHAR(255),
                      content_type      VARCHAR(100),
                      file_size         BIGINT,
                      uploaded_at       TIMESTAMP NOT NULL DEFAULT NOW(),

    -- Metadata
                      fotograaf         VARCHAR(255),
                      licentie          VARCHAR(255),
                      alt_tekst         VARCHAR(255),
                      hoofdfoto         BOOLEAN DEFAULT FALSE,
                      bron              VARCHAR(255),

                      CONSTRAINT fk_foto_plant_soorten
                          FOREIGN KEY (plant_soort_id) REFERENCES plant_soorten(id)
                              ON DELETE CASCADE
);

CREATE INDEX ix_foto_plant_soort_id
    ON foto (plant_soort_id);

-- Per plant max 1 hoofdfoto (PostgreSQL partial unique index)
CREATE UNIQUE INDEX ux_foto_hoofdfoto_per_plant
    ON foto (plant_soort_id)
    WHERE hoofdfoto = TRUE;


-- =========================================
-- 4) Synoniem (compositie bij PlantSoort)
-- =========================================
CREATE TABLE synoniem (
                          id             BIGSERIAL PRIMARY KEY,
                          plant_soort_id BIGINT NOT NULL,
                          naam           VARCHAR(255) NOT NULL,
                          CONSTRAINT fk_synoniem_plant_soorten
                              FOREIGN KEY (plant_soort_id) REFERENCES plant_soorten(id)
                                  ON DELETE CASCADE
);

-- Voorkom dubbele synoniemen per plant
CREATE UNIQUE INDEX ux_synoniem_plantsyn
    ON synoniem (plant_soort_id, LOWER(naam));

-- =========================================
-- 5) Kenmerken
-- =========================================
CREATE TABLE kenmerken (
                           id      BIGSERIAL PRIMARY KEY,
                           type    VARCHAR(255) NOT NULL,
                           waarde  VARCHAR(255) NOT NULL
);

-- Uniek kenmerk op (type, waarde) (case-insensitive)
CREATE UNIQUE INDEX ux_kenmerken_type_waarde
    ON kenmerken (LOWER(type), LOWER(waarde));

-- =========================================
-- 6) PlantKenmerk (koppelentiteit voor N-M)
-- =========================================
CREATE TABLE plantkenmerk (
                              id             BIGSERIAL PRIMARY KEY,
                              plant_soort_id BIGINT NOT NULL,
                              kenmerk_id     BIGINT NOT NULL,

                              CONSTRAINT fk_plantkenmerk_plant_soorten
                                  FOREIGN KEY (plant_soort_id) REFERENCES plant_soorten(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_plantkenmerk_kenmerken
                                  FOREIGN KEY (kenmerk_id) REFERENCES kenmerken(id)
                                      ON DELETE RESTRICT
);

-- Voorkom dubbele koppelingen
CREATE UNIQUE INDEX ux_plantkenmerk_unique_pair
    ON plantkenmerk (plant_soort_id, kenmerk_id);

-- =========================================
-- 7) Spelsessies (compositie bij Gebruiker)
-- =========================================
CREATE TABLE spelsessies (
                             id              BIGSERIAL PRIMARY KEY,
                             gebruiker_id    BIGINT NOT NULL,
                             modus           VARCHAR(80) NOT NULL,
                             level           INTEGER,
                             score           INTEGER,
                             duur_sec        INTEGER,
                             aantal_correct  INTEGER,
                             aantal_pogingen INTEGER,
                             gespeeld_op     TIMESTAMP NOT NULL DEFAULT NOW(),

                             CONSTRAINT fk_spelsessies_gebruikers
                                 FOREIGN KEY (gebruiker_id) REFERENCES gebruikers(id)
                                     ON DELETE CASCADE
);

CREATE INDEX ix_spelsessies_gebruiker_id
    ON spelsessies (gebruiker_id);

-- =========================================
-- 8) Favorieten (compositie bij Gebruiker, verwijst naar PlantSoort)
-- =========================================
CREATE TABLE favorieten (
                            id             BIGSERIAL PRIMARY KEY,
                            gebruiker_id   BIGINT NOT NULL,
                            plant_soort_id BIGINT NOT NULL,
                            aangemaakt_op  TIMESTAMP NOT NULL DEFAULT NOW(),

                            CONSTRAINT fk_favorieten_gebruikers
                                FOREIGN KEY (gebruiker_id) REFERENCES gebruikers(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_favorieten_plant_soorten
                                FOREIGN KEY (plant_soort_id) REFERENCES plant_soorten(id)
                                    ON DELETE RESTRICT
);

-- Voorkom dubbele favorieten per gebruiker
CREATE UNIQUE INDEX ux_favorieten_unique_pair
    ON favorieten (gebruiker_id, plant_soort_id);

CREATE INDEX ix_favorieten_gebruiker_id
    ON favorieten (gebruiker_id);

CREATE INDEX ix_favorieten_plant_soort_id
    ON favorieten (plant_soort_id);

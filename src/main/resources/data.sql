-- =========================================
-- data.sql - Seed data voor Plantenkennis API
-- =========================================

-- =========================================
-- 0) Opschonen (volgorde i.v.m. FK’s)
-- =========================================
DELETE FROM favorieten;
DELETE FROM spelsessies;
DELETE FROM plantkenmerk;
DELETE FROM foto;
DELETE FROM synoniem;
DELETE FROM kenmerken;
DELETE FROM plant_soorten;
DELETE FROM gebruikers;

-- =========================================
-- 1) PlantSoorten (18 stuks)
-- =========================================
INSERT INTO plant_soorten (
    wetenschappelijke_naam,
    nederlandse_naam,
    familie,
    beschrijving,
    bloeiperiode_start,
    bloeiperiode_einde,
    giftig,
    inheems,
    onderhoudsniveau,
    slug,
    updated_at
) VALUES
-- SIERPLANTEN (6)
('Lavandula angustifolia', 'Lavendel', 'Lamiaceae',
 'Geurende paarse bloeier; trekt bijen en vlinders aan.', 6, 8, false, false, 'LAAG', 'lavendel', NOW()),
('Rosa rubiginosa', 'Hondsroos', 'Rosaceae',
 'Wilde roos met enkelvoudige bloemen en bottels.', 6, 7, false, true, 'GEMIDDELD', 'hondsroos', NOW()),
('Hydrangea macrophylla', 'Boerenhortensia', 'Hydrangeaceae',
 'Grote bolvormige bloemen; kleur afhankelijk van bodem.', 6, 9, true, false, 'GEMIDDELD', 'boerenhortensia', NOW()),
('Tulipa gesneriana', 'Tulp', 'Liliaceae',
 'Voorjaarsbol met felle kleuren.', 4, 5, true, false, 'LAAG', 'tulp', NOW()),
('Narcissus pseudonarcissus', 'Narcis', 'Amaryllidaceae',
 'Gele voorjaarsbloeier; verwildert goed.', 3, 4, true, true, 'LAAG', 'narcis', NOW()),
('Helianthus annuus', 'Zonnebloem', 'Asteraceae',
 'Hoge plant met grote gele bloem.', 7, 9, false, false, 'LAAG', 'zonnebloem', NOW()),

-- KRUIDEN (6)
('Ocimum basilicum', 'Basilicum', 'Lamiaceae',
 'Aromatisch kruid voor mediterrane gerechten.', 6, 9, false, false, 'LAAG', 'basilicum', NOW()),
('Petroselinum crispum', 'Peterselie', 'Apiaceae',
 'Veelgebruikt keukenkruid.', 4, 10, false, false, 'LAAG', 'peterselie', NOW()),
('Thymus vulgaris', 'Tijm', 'Lamiaceae',
 'Sterk aromatisch kruid; winterhard.', 5, 9, false, false, 'LAAG', 'tijm', NOW()),
('Mentha spicata', 'Munt', 'Lamiaceae',
 'Snelgroeiend kruid; geschikt voor thee.', 5, 10, false, false, 'GEMIDDELD', 'munt', NOW()),
('Rosmarinus officinalis', 'Rozemarijn', 'Lamiaceae',
 'Houtig kruid; houdt van zon.', 5, 8, false, false, 'GEMIDDELD', 'rozemarijn', NOW()),
('Salvia officinalis', 'Salie', 'Lamiaceae',
 'Grijsgroen kruid met sterke smaak.', 6, 8, false, false, 'LAAG', 'salie', NOW()),

-- BOMEN (6)
('Quercus robur', 'Zomereik', 'Fagaceae',
 'Inheemse boom; belangrijk voor biodiversiteit.', NULL, NULL, false, true, 'LAAG', 'zomereik', NOW()),
('Betula pendula', 'Ruwe berk', 'Betulaceae',
 'Sierlijke boom met witte bast.', NULL, NULL, false, true, 'LAAG', 'ruwe-berk', NOW()),
('Fagus sylvatica', 'Beuk', 'Fagaceae',
 'Grote boom met gladde bast.', NULL, NULL, false, true, 'LAAG', 'beuk', NOW()),
('Acer platanoides', 'Noorse esdoorn', 'Sapindaceae',
 'Laanboom met handvormige bladeren.', NULL, NULL, false, false, 'LAAG', 'noorse-esdoorn', NOW()),
('Tilia cordata', 'Winterlinde', 'Malvaceae',
 'Boom met hartvormige bladeren; bijvriendelijk.', 6, 7, false, true, 'LAAG', 'winterlinde', NOW()),
('Malus domestica', 'Appelboom', 'Rosaceae',
 'Fruitboom met bloesem en appels.', 4, 5, false, false, 'GEMIDDELD', 'appelboom', NOW());

-- =========================================
-- 2) Kenmerken (herbruikbaar)
-- =========================================
INSERT INTO kenmerken (type, waarde) VALUES
                                       ('BLOEMKLEUR', 'PAARS'),
                                       ('BLOEMKLEUR', 'GEEL'),
                                       ('BLOEMKLEUR', 'WIT'),
                                       ('BLOEMKLEUR', 'ROOD'),

                                       ('STANDPLAATS', 'ZON'),
                                       ('STANDPLAATS', 'HALFSCHADUW'),
                                       ('STANDPLAATS', 'SCHADUW'),

                                       ('WATERBEHOEFTE', 'LAAG'),
                                       ('WATERBEHOEFTE', 'GEMIDDELD'),
                                       ('WATERBEHOEFTE', 'HOOG');

-- =========================================
-- 3) Koppelingen Plant ↔ Kenmerk (ruimer voor testen)
-- (subselects op naam → stabiel)
-- =========================================
INSERT INTO plantkenmerk (plant_soort_id, kenmerk_id)
SELECT ps.id, k.id
FROM plant_soorten ps
         JOIN kenmerken k ON 1=1
WHERE
   -- Lavendel: paars, zon, laag water
    (ps.nederlandse_naam = 'Lavendel' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'PAARS')
   OR (ps.nederlandse_naam = 'Lavendel' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Lavendel' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'LAAG')

   -- Hondsroos: rood, zon/halfschaduw, gemiddeld water
   OR (ps.nederlandse_naam = 'Hondsroos' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'ROOD')
   OR (ps.nederlandse_naam = 'Hondsroos' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Hondsroos' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'GEMIDDELD')

   -- Boerenhortensia: wit, schaduw/halfschaduw, hoog water
   OR (ps.nederlandse_naam = 'Boerenhortensia' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'WIT')
   OR (ps.nederlandse_naam = 'Boerenhortensia' AND k.type = 'STANDPLAATS' AND k.waarde = 'SCHADUW')
   OR (ps.nederlandse_naam = 'Boerenhortensia' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'HOOG')

   -- Tulp: rood, zon, gemiddeld water
   OR (ps.nederlandse_naam = 'Tulp' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'ROOD')
   OR (ps.nederlandse_naam = 'Tulp' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Tulp' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'GEMIDDELD')

   -- Narcis: geel, zon/halfschaduw, laag water
   OR (ps.nederlandse_naam = 'Narcis' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'GEEL')
   OR (ps.nederlandse_naam = 'Narcis' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Narcis' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'LAAG')

   -- Zonnebloem: geel, zon, hoog water
   OR (ps.nederlandse_naam = 'Zonnebloem' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'GEEL')
   OR (ps.nederlandse_naam = 'Zonnebloem' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Zonnebloem' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'HOOG')

   -- Basilicum: wit (bloemetjes), zon, hoog water
   OR (ps.nederlandse_naam = 'Basilicum' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'WIT')
   OR (ps.nederlandse_naam = 'Basilicum' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Basilicum' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'HOOG')

   -- Peterselie: zon/halfschaduw, gemiddeld water
   OR (ps.nederlandse_naam = 'Peterselie' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Peterselie' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'GEMIDDELD')

   -- Tijm: zon, laag water
   OR (ps.nederlandse_naam = 'Tijm' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Tijm' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'LAAG')

   -- Munt: halfschaduw, hoog water
   OR (ps.nederlandse_naam = 'Munt' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Munt' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'HOOG')

   -- Rozemarijn: zon, laag water
   OR (ps.nederlandse_naam = 'Rozemarijn' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Rozemarijn' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'LAAG')

   -- Salie: zon, laag water
   OR (ps.nederlandse_naam = 'Salie' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')
   OR (ps.nederlandse_naam = 'Salie' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'LAAG')

   -- Zomereik: zon/halfschaduw
   OR (ps.nederlandse_naam = 'Zomereik' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')

   -- Ruwe berk: zon
   OR (ps.nederlandse_naam = 'Ruwe berk' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')

   -- Beuk: halfschaduw / schaduw
   OR (ps.nederlandse_naam = 'Beuk' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Beuk' AND k.type = 'STANDPLAATS' AND k.waarde = 'SCHADUW')

   -- Noorse esdoorn: zon/halfschaduw
   OR (ps.nederlandse_naam = 'Noorse esdoorn' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')

   -- Winterlinde: zon/halfschaduw, wit (bloesem)
   OR (ps.nederlandse_naam = 'Winterlinde' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'WIT')
   OR (ps.nederlandse_naam = 'Winterlinde' AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON')

   -- Appelboom: wit (bloesem), zon/halfschaduw, gemiddeld water
   OR (ps.nederlandse_naam = 'Appelboom' AND k.type = 'BLOEMKLEUR' AND k.waarde = 'WIT')
   OR (ps.nederlandse_naam = 'Appelboom' AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW')
   OR (ps.nederlandse_naam = 'Appelboom' AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'GEMIDDELD');

-- =========================================
-- 4) Foto’s (incl. hoofdfoto + extra foto’s)
-- Let op: foto zelf staat niet in DB, alleen metadata + storage_path.
-- storage_path: plantsoorten/<plantSoortId>/<filename>
-- url: http://localhost:8080/uploads/plantsoorten/<plantSoortId>/<filename>
-- =========================================

-- Lavendel: 1 hoofdfoto + 1 extra
INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/lavendel-1.jpg',
    'plantsoorten/' || ps.id || '/lavendel-1.jpg',
    'lavendel-1.jpg',
    'image/jpeg',
    123456,
    NOW() - INTERVAL '10 days',
    'Seed',
    'CC-BY',
    'Lavendel in bloei',
    TRUE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Lavendel';

INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/lavendel-2.jpg',
    'plantsoorten/' || ps.id || '/lavendel-2.jpg',
    'lavendel-2.jpg',
    'image/jpeg',
    112233,
    NOW() - INTERVAL '9 days',
    'Seed',
    'CC-BY',
    'Lavendel close-up',
    FALSE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Lavendel';


-- Zonnebloem: 1 hoofdfoto
INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/zonnebloem-1.jpg',
    'plantsoorten/' || ps.id || '/zonnebloem-1.jpg',
    'zonnebloem-1.jpg',
    'image/jpeg',
    223344,
    NOW() - INTERVAL '8 days',
    'Seed',
    'CC0',
    'Zonnebloem in de zon',
    TRUE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Zonnebloem';


-- Basilicum: 1 hoofdfoto + 1 extra
INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/basilicum-1.jpg',
    'plantsoorten/' || ps.id || '/basilicum-1.jpg',
    'basilicum-1.jpg',
    'image/jpeg',
    99887,
    NOW() - INTERVAL '7 days',
    'Seed',
    'CC-BY',
    'Basilicumplant',
    TRUE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Basilicum';

INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/basilicum-2.jpg',
    'plantsoorten/' || ps.id || '/basilicum-2.jpg',
    'basilicum-2.jpg',
    'image/jpeg',
    88776,
    NOW() - INTERVAL '6 days',
    'Seed',
    'CC-BY',
    'Basilicum bladeren',
    FALSE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Basilicum';


-- Beuk: 1 foto (geen hoofdfoto) → test: plant met foto’s maar geen hoofdfoto
INSERT INTO foto (
    plant_soort_id, url,
    storage_path, original_filename, content_type, file_size, uploaded_at,
    fotograaf, licentie, alt_tekst, hoofdfoto, bron
)
SELECT
    ps.id,
    'http://localhost:8080/uploads/plantsoorten/' || ps.id || '/beuk-1.jpg',
    'plantsoorten/' || ps.id || '/beuk-1.jpg',
    'beuk-1.jpg',
    'image/jpeg',
    445566,
    NOW() - INTERVAL '5 days',
    'Seed',
    'CC-BY',
    'Beuk blad',
    FALSE,
    'seed'
FROM plant_soorten ps
WHERE ps.nederlandse_naam = 'Beuk';

-- Let op: sommige planten houden we expres ZONDER foto voor testcases.

-- =========================================
-- 5) Synoniemen (handig voor toekomstige endpoints)
-- =========================================
INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Echte lavendel' FROM plant_soorten WHERE nederlandse_naam = 'Lavendel';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Zonnebloem (eenjarig)' FROM plant_soorten WHERE nederlandse_naam = 'Zonnebloem';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Keukenbasilicum' FROM plant_soorten WHERE nederlandse_naam = 'Basilicum';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Gewone narcis' FROM plant_soorten WHERE nederlandse_naam = 'Narcis';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Appel (cultivar)' FROM plant_soorten WHERE nederlandse_naam = 'Appelboom';


-- =========================================
-- 6) Gebruikers (voor spelsessies & favorieten tests)
-- =========================================
INSERT INTO gebruikers (keycloak_subject, naam, email, wachtwoord_hash, created_at)
VALUES
    ('dev-admin-001', 'beheerder1', 'beheerder@example.com', NULL, NOW() - INTERVAL '10 days'),
    ('dev-user-001', 'deelnemer1', 'speler@example.com', NULL, NOW() - INTERVAL '5 days'),
    ('dev-guest-001', 'Gast Gebruiker', 'gast@example.com', NULL, NOW() - INTERVAL '1 day');

-- =========================================
-- 7) Spelsessies (koppelen aan gebruikers)
-- =========================================

-- 3 sessies voor Koen
INSERT INTO spelsessies (
    gebruiker_id, modus, level, score, duur_sec, aantal_correct, aantal_pogingen, gespeeld_op
)
SELECT
    g.id, 'TRAINING', 1, 120, 90, 8, 10, NOW() - INTERVAL '2 days'
FROM gebruikers g
WHERE g.email = 'koen@example.com';

INSERT INTO spelsessies (
    gebruiker_id, modus, level, score, duur_sec, aantal_correct, aantal_pogingen, gespeeld_op
)
SELECT
    g.id, 'TRAINING', 2, 180, 110, 12, 15, NOW() - INTERVAL '1 day'
FROM gebruikers g
WHERE g.email = 'koen@example.com';

INSERT INTO spelsessies (
    gebruiker_id, modus, level, score, duur_sec, aantal_correct, aantal_pogingen, gespeeld_op
)
SELECT
    g.id, 'EXAMEN', 3, 250, 140, 18, 20, NOW() - INTERVAL '3 hours'
FROM gebruikers g
WHERE g.email = 'koen@example.com';

-- 2 sessies voor Test Speler
INSERT INTO spelsessies (
    gebruiker_id, modus, level, score, duur_sec, aantal_correct, aantal_pogingen, gespeeld_op
)
SELECT
    g.id, 'TRAINING', 1, 90, 75, 6, 12, NOW() - INTERVAL '5 hours'
FROM gebruikers g
WHERE g.email = 'speler@example.com';

INSERT INTO spelsessies (
    gebruiker_id, modus, level, score, duur_sec, aantal_correct, aantal_pogingen, gespeeld_op
)
SELECT
    g.id, 'EXAMEN', 2, 160, 105, 11, 14, NOW() - INTERVAL '30 minutes'
FROM gebruikers g
WHERE g.email = 'speler@example.com';

-- 0 sessies voor Gast Gebruiker (bewust leeg voor testcases)

-- =========================================
-- 8) Favorieten (koppelen aan gebruiker + plantsoort)
--    LET OP: plant_soort_id verwijst naar plant_soorten.id
-- =========================================

-- Koen: 2 favorieten
INSERT INTO favorieten (gebruiker_id, plant_soort_id, aangemaakt_op)
SELECT
    g.id,
    p.id,
    NOW() - INTERVAL '4 days'
FROM gebruikers g
    JOIN plant_soorten p ON p.nederlandse_naam = 'Lavendel'
WHERE g.email = 'koen@example.com';

INSERT INTO favorieten (gebruiker_id, plant_soort_id, aangemaakt_op)
SELECT
    g.id,
    p.id,
    NOW() - INTERVAL '2 days'
FROM gebruikers g
    JOIN plant_soorten p ON p.nederlandse_naam = 'Beuk'
WHERE g.email = 'koen@example.com';

-- Test Speler: 1 favoriet
INSERT INTO favorieten (gebruiker_id, plant_soort_id, aangemaakt_op)
SELECT
    g.id,
    p.id,
    NOW() - INTERVAL '1 day'
FROM gebruikers g
    JOIN plant_soorten p ON p.nederlandse_naam = 'Zonnebloem'
WHERE g.email = 'speler@example.com';

-- Gast: 1 favoriet
INSERT INTO favorieten (gebruiker_id, plant_soort_id, aangemaakt_op)
SELECT
    g.id,
    p.id,
    NOW() - INTERVAL '12 hours'
FROM gebruikers g
    JOIN plant_soorten p ON p.nederlandse_naam = 'Tulp'
WHERE g.email = 'gast@example.com';

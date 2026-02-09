-- =========================================
-- data.sql - Seed data voor Plantenkennis API
-- =========================================

-- =========================================
-- 0) Opschonen (volgorde i.v.m. FK’s)
-- =========================================
DELETE FROM favoriet;
DELETE FROM spelsessie;
DELETE FROM plantkenmerk;
DELETE FROM foto;
DELETE FROM synoniem;
DELETE FROM kenmerk;
DELETE FROM plant_soort;

-- =========================================
-- 1) PlantSoorten (18 stuks)
-- =========================================
INSERT INTO plant_soort (
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
INSERT INTO kenmerk (type, waarde) VALUES
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
FROM plant_soort ps
         JOIN kenmerk k ON 1=1
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
-- Belangrijk: per plant max 1 hoofdfoto (partial unique index)
-- =========================================

-- Lavendel: 1 hoofdfoto + 1 extra
INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/lavendel-1.jpg', 'Seed', 'CC-BY', 'Lavendel in bloei', TRUE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Lavendel';

INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/lavendel-2.jpg', 'Seed', 'CC-BY', 'Lavendel close-up', FALSE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Lavendel';

-- Zonnebloem: 1 hoofdfoto
INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/zonnebloem-1.jpg', 'Seed', 'CC0', 'Zonnebloem in de zon', TRUE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Zonnebloem';

-- Basilicum: 1 hoofdfoto + 1 extra
INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/basilicum-1.jpg', 'Seed', 'CC-BY', 'Basilicumplant', TRUE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Basilicum';

INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/basilicum-2.jpg', 'Seed', 'CC-BY', 'Basilicum bladeren', FALSE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Basilicum';

-- Beuk: 1 foto (geen hoofdfoto) → test: plant met foto’s maar geen hoofdfoto
INSERT INTO foto (plant_soort_id, url, fotograaf, licentie, alt_tekst, hoofdfoto, bron)
SELECT id, 'https://example.com/beuk-1.jpg', 'Seed', 'CC-BY', 'Beuk blad', FALSE, 'seed'
FROM plant_soort WHERE nederlandse_naam = 'Beuk';

-- Let op: sommige planten houden we expres ZONDER foto voor testcases.

-- =========================================
-- 5) Synoniemen (handig voor toekomstige endpoints)
-- =========================================
INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Echte lavendel' FROM plant_soort WHERE nederlandse_naam = 'Lavendel';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Zonnebloem (eenjarig)' FROM plant_soort WHERE nederlandse_naam = 'Zonnebloem';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Keukenbasilicum' FROM plant_soort WHERE nederlandse_naam = 'Basilicum';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Gewone narcis' FROM plant_soort WHERE nederlandse_naam = 'Narcis';

INSERT INTO synoniem (plant_soort_id, naam)
SELECT id, 'Appel (cultivar)' FROM plant_soort WHERE nederlandse_naam = 'Appelboom';

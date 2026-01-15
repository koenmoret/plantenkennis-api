-- =========================================
-- 0) Opschonen (volgorde i.v.m. FK’s)
-- =========================================
DELETE FROM plantkenmerk;
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
-- 3) Koppelingen Plant ↔ Kenmerk
-- (subselects op naam → stabiel)
-- =========================================
INSERT INTO plantkenmerk (plant_soort_id, kenmerk_id)
SELECT ps.id, k.id
FROM plant_soort ps, kenmerk k
WHERE
    (
        ps.nederlandse_naam = 'Lavendel'
            AND k.type = 'BLOEMKLEUR' AND k.waarde = 'PAARS'
        )
   OR (
    ps.nederlandse_naam = 'Lavendel'
        AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON'
    )
   OR (
    ps.nederlandse_naam = 'Basilicum'
        AND k.type = 'WATERBEHOEFTE' AND k.waarde = 'HOOG'
    )
   OR (
    ps.nederlandse_naam = 'Zonnebloem'
        AND k.type = 'STANDPLAATS' AND k.waarde = 'ZON'
    )
   OR (
    ps.nederlandse_naam = 'Narcis'
        AND k.type = 'BLOEMKLEUR' AND k.waarde = 'GEEL'
    )
   OR (
    ps.nederlandse_naam = 'Beuk'
        AND k.type = 'STANDPLAATS' AND k.waarde = 'HALFSCHADUW'
    );

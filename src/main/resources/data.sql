-- Leeg de tabel (alleen als hij bestaat)
DELETE FROM plant_soorten;

-- Seed data (Variant A – id wordt automatisch gegenereerd)
INSERT INTO plant_soorten (latijnse_naam, naam, omschrijving, soort) VALUES
-- SIERPLANT (6)
('Lavandula angustifolia', 'Lavendel', 'Geurende paarse bloeier; trekt bijen en vlinders aan.', 'SIERPLANT'),
('Rosa rubiginosa', 'Hondsroos', 'Klassieke roos met enkelvoudige bloemen; kan doorns hebben.', 'SIERPLANT'),
('Hydrangea macrophylla', 'Boerenhortensia', 'Grote bloemschermen; kleur kan variëren met de zuurgraad van de bodem.', 'SIERPLANT'),
('Tulipa gesneriana', 'Tulp', 'Voorjaarsbol met opvallende bloemen in veel kleuren.', 'SIERPLANT'),
('Narcissus pseudonarcissus', 'Narcis', 'Voorjaarsbloeier met gele trompetbloem; verwildert goed.', 'SIERPLANT'),
('Helianthus annuus', 'Zonnebloem', 'Hoge plant met grote gele bloem; populair in tuinen.', 'SIERPLANT'),

-- KRUID (6)
('Ocimum basilicum', 'Basilicum', 'Aromatisch kruid; veel gebruikt in Italiaanse gerechten.', 'KRUID'),
('Petroselinum crispum', 'Peterselie', 'Fris groen kruid; geschikt als garnering en smaakmaker.', 'KRUID'),
('Thymus vulgaris', 'Tijm', 'Houtig kruid met kleine blaadjes; sterk aroma.', 'KRUID'),
('Mentha spicata', 'Munt', 'Snelgroeiend kruid; geschikt voor thee en desserts.', 'KRUID'),
('Rosmarinus officinalis', 'Rozemarijn', 'Mediterrane struik; naaldachtige blaadjes met krachtige smaak.', 'KRUID'),
('Salvia officinalis', 'Salie', 'Kruid met grijsgroene bladeren; past goed bij vlees- en groentegerechten.', 'KRUID'),

-- BOOM (6)
('Quercus robur', 'Zomereik', 'Inheemse boom; belangrijke soort voor biodiversiteit.', 'BOOM'),
('Betula pendula', 'Ruwe berk', 'Sierlijke boom met witte bast; groeit vaak op arme gronden.', 'BOOM'),
('Fagus sylvatica', 'Beuk', 'Grote boom met gladde grijze bast; kan tot heg worden gesnoeid.', 'BOOM'),
('Acer platanoides', 'Noorse esdoorn', 'Boom met handvormige bladeren; vaak toegepast als laanboom.', 'BOOM'),
('Tilia cordata', 'Winterlinde', 'Boom met hartvormige bladeren; bloemen zijn geliefd bij bijen.', 'BOOM'),
('Malus domestica', 'Appelboom', 'Fruitboom met bloesem in het voorjaar en appels in het najaar.', 'BOOM');

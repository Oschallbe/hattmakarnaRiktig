    -- Skript

    -- använder default databasschemat för att kunna köra koden nedan
    use mysql;

    -- Ta bort proceduren om den redan finns, möjliggör att köra om koden igen utan krångel
    DROP PROCEDURE IF EXISTS setup_hattmakaren;
    DELIMITER //


    -- Skapar en procedure som kan köras för att skapa ett databasschema och en användare och ger användaren rättigheter till schemat.
    -- Om databasschemat finns sedan tidigare tas det bort och återskapas
    -- Om användare finns skapar inte användaren igen.
    CREATE PROCEDURE setup_hattmakaren()
    BEGIN
        DECLARE user_exists INT DEFAULT 0;

        -- Skapa databasen om den inte redan finns
        DROP DATABASE IF EXISTS hattmakaren;
        CREATE DATABASE hattmakaren;

        -- Kontrollera om användaren 'dbAdmin2024'@'localhost' finns
        SELECT COUNT(*) INTO user_exists
        FROM mysql.user
        WHERE user = 'dbHattAdmin' AND host = 'localhost';

        -- Om användaren inte finns, skapa användaren och tilldela behörigheter
        IF user_exists = 0 THEN
            CREATE USER 'dbHattAdmin'@'localhost' IDENTIFIED BY 'dbHattAdminPW';
            GRANT ALL PRIVILEGES ON hattmakaren.* TO 'dbHattAdmin'@'localhost';
            FLUSH PRIVILEGES;
        END IF;
    END;
    //
    DELIMITER ;
    -- Kör proceduren
    call setup_hattmakaren();
    USE hattmakaren;

    SET SQL_SAFE_UPDATES = 0;

    CREATE TABLE Statistik (
        StatistikID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100),
        Typ VARCHAR(50)
    );

    CREATE TABLE Kund (
        KundID INT AUTO_INCREMENT PRIMARY KEY,
        Fornamn VARCHAR(50),
        Efternamn VARCHAR(50),
        Epost VARCHAR(100),
        Telefonnummer VARCHAR(20),
        Ort VARCHAR(100),
        LeveransAdress VARCHAR(255),
        FakturaAdress VARCHAR(255),
        StatistikID INT,
        FOREIGN KEY (StatistikID) REFERENCES Statistik(StatistikID)
    );



    CREATE TABLE Fraktsedel (
        FraktsedelID INT AUTO_INCREMENT PRIMARY KEY,
        Adress VARCHAR(255),
        Avsandare VARCHAR(100),
        Mottagare VARCHAR(100),
        Innehåll VARCHAR(1000),
        Exportkod VARCHAR(20),
        Pris DOUBLE,
        Datum DATE,
        Vikt DECIMAL(5,2),
        Moms DOUBLE,
        PrisInklMoms DOUBLE
    );

    CREATE TABLE Bestallning (
        BestallningID INT AUTO_INCREMENT PRIMARY KEY,
        Status VARCHAR(50),
        Datum DATE,
        Expressbestallning BOOLEAN,
        KundID INT,
        FraktsedelID INT,
        FOREIGN KEY (KundID) REFERENCES Kund(KundID),
        FOREIGN KEY (FraktsedelID) REFERENCES Fraktsedel(FraktsedelID)
    );

    CREATE TABLE StandardProdukt (
        StandardProduktID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100),
        Modell VARCHAR(100),
        Typ VARCHAR(50),
        Farg VARCHAR(50),
        Text VARCHAR(1000),
        Dekoration VARCHAR(1000),
        Storlek VARCHAR(20),
        Pris DOUBLE,
        StatistikID INT,
        FOREIGN KEY (StatistikID) REFERENCES Statistik(StatistikID)
    );

    CREATE TABLE SpecialProdukt (
        SpecialProduktID INT AUTO_INCREMENT PRIMARY KEY,
        Storlek VARCHAR(20),
        Farg VARCHAR(50),
        Typ VARCHAR(50),
        Modell VARCHAR(100),
        Text VARCHAR(1000),
        Dekoration VARCHAR(1000),
        Pris DOUBLE,
        Beskrivning VARCHAR(1000),
        Tillverkningstid VARCHAR(1000),
        StatistikID INT,
        FOREIGN KEY (StatistikID) REFERENCES Statistik(StatistikID)
    );

    CREATE TABLE ProduktionsSchema (
        ProduktionsSchemaID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100)
    );

    CREATE TABLE Anstalld (
        AnstalldID INT AUTO_INCREMENT PRIMARY KEY,
        Fornamn VARCHAR(50),
        Efternamn VARCHAR(50),
        Losenord VARCHAR(255),
        Epost VARCHAR(255),
        Behorighet INT,
        ProduktionsSchemaID INT,
        FOREIGN KEY (ProduktionsSchemaID) REFERENCES ProduktionsSchema(ProduktionsSchemaID)
    );

    CREATE TABLE MaterialBestallning (
        MaterialBestallningsID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100),
        Antal INT,
        BestallningsDatum DATE
    );

    CREATE TABLE Material (
        MaterialID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100),
        Typ VARCHAR(50),
        Farg VARCHAR(50),
        Pris DECIMAL(10,2),
        StandardProduktID INT,
        SpecialProduktID INT,
        MaterialBestallningsID INT,
        FOREIGN KEY (StandardProduktID) REFERENCES StandardProdukt(StandardProduktID),
        FOREIGN KEY (SpecialProduktID) REFERENCES SpecialProdukt(SpecialProduktID),
        FOREIGN KEY (MaterialBestallningsID) REFERENCES MaterialBestallning(MaterialBestallningsID)
    );

    CREATE TABLE OrderItem (
        OrderItemID INT AUTO_INCREMENT PRIMARY KEY,
        AntalProdukter INT,
        BestallningID INT,
        StandardProduktID INT,
        SpecialProduktID INT,
        ProduktionsSchemaID INT,
        AnstalldID INT,
        FOREIGN KEY (BestallningID) REFERENCES Bestallning(BestallningID),
        FOREIGN KEY (StandardProduktID) REFERENCES StandardProdukt(StandardProduktID),
        FOREIGN KEY (SpecialProduktID) REFERENCES SpecialProdukt(SpecialProduktID),
        FOREIGN KEY (ProduktionsSchemaID) REFERENCES ProduktionsSchema(ProduktionsSchemaID),
        FOREIGN KEY (AnstalldID) REFERENCES Anstalld(AnstalldID)
    );



    INSERT INTO Statistik (Namn, Typ) VALUES
    ('Månadsförsäljning av studentmössor – Hattmakarna AB', 'Försäljningsstatistik'),
    ('Försäljning av cosplay-hattar – Hattmakarna AB', 'Försäljningsstatistik'),
    ('Intäkter per hattmodell', 'Försäljningsstatistik'),
    ('Försäljning per stad', 'Försäljningsstatistik'),
    ('Topp 10 mest sålda hattar 2025', 'Försäljningsstatistik');

    INSERT INTO ProduktionsSchema (Namn) VALUES
    ('Studentmössor – Dagsskift'),
    ('Cosplayhattar – Kvällsskift'),
    ('Skräddarsydda hattar – Helgteam'),
    ('Expresshattar – Nattpass'),
    ('Specialprojekt – Temporärt schema');

    INSERT INTO Anstalld (Fornamn, Efternamn, Losenord, Epost, Behorighet, ProduktionsSchemaID) VALUES
    ('Karin', 'Hattlund', 'hatt123', 'karin@hattmakarna.se', 1, 1),
    ('Ella', 'Sticksson', 'hatthemlig', 'ella@hattmakarna.se', 1, 2),
    ('Maja', 'Syberg', 'maja123', 'maja@hattmakarna.se', 1, 3),
    ('Jens', 'Krona', 'jens456', 'jens@hattmakarna.se', 1, 4),
    ('Otto', 'Formgren', 'adminpass', 'otto@hattmakarna.se', 2, 5);  -- ADMIN

    INSERT INTO Kund (Fornamn, Efternamn, Epost, Telefonnummer, Ort, LeveransAdress, FakturaAdress, StatistikID) VALUES
    ('Julia', 'Sundström', 'julia.sundstrom@gmail.com', '0701112233', 'Stockholm', 'Studentvägen 7', 'Studentvägen 7', 1),
    ('Felix', 'Norberg', 'felix.norberg@gmail.com', '0739988776', 'Lund', 'Cosplaygatan 12', 'Cosplaygatan 12', 2),
    ('Agnes', 'Eriksson', 'agnes.eriksson@gmail.com', '0723456789', 'Uppsala', 'Hattvägen 10', 'Hattvägen 10', 3),
    ('Viktor', 'Karlsson', 'viktor.karlsson@gmail.com', '0765544332', 'Göteborg', 'Gustavsgatan 5', 'Gustavsgatan 5', 4),
    ('Elin', 'Blom', 'elin.blom@gmail.com', '0709988776', 'Malmö', 'Rosengatan 3', 'Rosengatan 3', 5);

    INSERT INTO Fraktsedel (Adress, Avsandare, Mottagare, Innehåll, Exportkod, Pris, Datum, Vikt, Moms, PrisInklMoms) VALUES
    ('Studentvägen 7, Stockholm', 'Hattmakarna AB', 'Julia Sundström', 'Studentmössa Klassisk', 'HATT2025', 79, '2025-04-10', 0.4, 19.75, 98.75),
    ('Cosplaygatan 12, Lund', 'Hattmakarna AB', 'Felix Norberg', 'Cosplay-hatt: Magiker', 'COSHATT2025', 99, '2025-04-11', 0.6, 24.75, 123.75),
    ('Hattvägen 10, Uppsala', 'Hattmakarna AB', 'Agnes Eriksson', 'Top Hat Deluxe', 'HATT2025EX', 120, '2025-04-12', 0.8, 30, 150),
    ('Gustavsgatan 5, Göteborg', 'Hattmakarna AB', 'Viktor Karlsson', 'Cap Deluxe', 'HATT2025CAP', 85, '2025-04-13', 0.5, 21.25, 106.25),
    ('Rosengatan 3, Malmö', 'Hattmakarna AB', 'Elin Blom', 'Cosplay-hatt: Rävöron', 'COSFOX2025', 110, '2025-04-14', 0.7, 27.5, 137.5);

    INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, FraktsedelID) VALUES
    ('Packas', '2025-04-10', TRUE, 1, 1),
    ('Skickad', '2025-04-11', FALSE, 2, 2),
    ('Produktion pågår', '2025-04-12', TRUE, 3, 3),
    ('Under behandling', '2025-04-13', FALSE, 4, 4),
    ('Levererad', '2025-04-14', TRUE, 5, 5);

    INSERT INTO StandardProdukt (Namn, Modell, Typ, Farg, Text, Dekoration, Storlek, Pris, StatistikID) VALUES
    ('Studentmössa Klassisk 2025', 'SM-25-KLASS', 'Studentmössa', 'Vit/Blå', 'Grattis!', 'Guldemblem', '57', 449, 1),
    ('Cosplay-hatt: Magiker', 'CH-MAGI', 'Cosplay', 'Svart/Lila', '', 'Stjärnor & fjädrar', '58', 599, 2),
    ('Sjömansmössa', 'SM-NAVY', 'Studentmössa', 'Vit', 'Naval Style', 'Blå kant', '56', 379, 1),
    ('Cosplay-hatt: Ninja', 'CH-NIN', 'Cosplay', 'Svart', '', 'Tygmask', '57', 489, 2),
    ('Festhatt glitter', 'FH-GLT', 'Party', 'Guld', 'Party Time!', 'Glitterband', '59', 129, 5);

    INSERT INTO SpecialProdukt (Storlek, Farg, Typ, Modell, Text, Dekoration, Pris, Beskrivning, Tillverkningstid, StatistikID) VALUES
    ('59', 'Vinröd', 'Skräddarsydd', 'Viktoriansk Hög Hatt', 'Lord Crimson', 'Spets & kedjor', 899, 'Hög cylinderhatt för steampunk-event', '10 dagar', 3),
    ('58', 'Svart', 'Skräddarsydd', 'Rävhatt Cosplay', '', 'Rävöron & päls', 799, 'Inspirerad av japansk folklore', '7 dagar', 2),
    ('60', 'Marinblå', 'Skräddarsydd', 'Kapten Deluxe', 'Kapten Z', 'Guldinslag & fjäder', 999, 'För karneval eller teater', '12 dagar', 4),
    ('56', 'Vit', 'Skräddarsydd', 'Mösspimpad', 'Student 2025', 'LED-ljus & text', 549, 'Mössa med specialeffekter', '5 dagar', 1),
    ('57', 'Grön', 'Skräddarsydd', 'Elf Edition', '', 'Klockor & glitter', 459, 'Julhatt för cosplay', '6 dagar', 5);

    INSERT INTO MaterialBestallning (Namn, Antal, BestallningsDatum) VALUES
    ('Studentmössetyg vit', 100, '2025-03-20'),
    ('Sammet vinröd', 50, '2025-03-22'),
    ('Glitterband guld', 200, '2025-03-25'),
    ('Tyg svart', 150, '2025-03-26'),
    ('Stjärndekor', 75, '2025-03-27');

    INSERT INTO Material (Namn, Typ, Farg, Pris, StandardProduktID, SpecialProduktID, MaterialBestallningsID) VALUES
    ('Mösstyg', 'Tyg', 'Vit', 10.00, 1, NULL, 1),
    ('Sammet', 'Tyg', 'Vinröd', 25.00, NULL, 1, 2),
    ('Glitterband', 'Dekoration', 'Guld', 5.00, 5, NULL, 3),
    ('Ninjatyg', 'Tyg', 'Svart', 15.00, 4, NULL, 4),
    ('Rävöron', 'Dekoration', 'Orange', 20.00, NULL, 2, 5);

    INSERT INTO OrderItem (AntalProdukter, BestallningID, StandardProduktID, SpecialProduktID, ProduktionsSchemaID, AnstalldID) VALUES
    (1, 1, 1, NULL, 1, 1),
    (1, 2, 2, NULL, 2, 2),
    (1, 3, NULL, 1, 3, 3),
    (2, 4, 4, NULL, 4, 4),
    (1, 5, NULL, 2, 5, 5);



    ALTER TABLE StandardProdukt
    ADD Artikelnummer VARCHAR(10);

    UPDATE StandardProdukt SET Artikelnummer = NULL;


    UPDATE StandardProdukt SET Artikelnummer = '669040' WHERE StandardProduktID = 1;
    UPDATE StandardProdukt SET Artikelnummer = '218501' WHERE StandardProduktID = 2;
    UPDATE StandardProdukt SET Artikelnummer = '169564' WHERE StandardProduktID = 3;
    UPDATE StandardProdukt SET Artikelnummer = '253516' WHERE StandardProduktID = 4;
    UPDATE StandardProdukt SET Artikelnummer = '920612' WHERE StandardProduktID = 5;


    INSERT INTO Anstalld (Fornamn, Efternamn, Losenord, Epost, Behorighet, ProduktionsSchemaID)
    VALUES ('Judith', 'Okänd', 'judithpw', 'judith@hattmakarna.se', 2, 1);

    UPDATE Anstalld
    SET Efternamn = 'Hattberg'
    WHERE Epost = 'judith@hattmakarna.se';

    ALTER TABLE StandardProdukt
    ADD Matt INT;

    UPDATE StandardProdukt SET Matt = 57 WHERE StandardProduktID = 1;
    UPDATE StandardProdukt SET Matt = 59 WHERE StandardProduktID = 2;
    UPDATE StandardProdukt SET Matt = 56 WHERE StandardProduktID = 3;
    UPDATE StandardProdukt SET Matt = 58 WHERE StandardProduktID = 4;
    UPDATE StandardProdukt SET Matt = 60 WHERE StandardProduktID = 5;

    ALTER TABLE Bestallning
    ADD COLUMN TotalPris DOUBLE;

    UPDATE Bestallning SET TotalPris = 449 WHERE BestallningID = 1;
    UPDATE Bestallning SET TotalPris = 599 WHERE BestallningID = 2;
    UPDATE Bestallning SET TotalPris = 899 WHERE BestallningID = 3;
    UPDATE Bestallning SET TotalPris = 978 WHERE BestallningID = 4;
    UPDATE Bestallning SET TotalPris = 799 WHERE BestallningID = 5;

    ALTER TABLE Bestallning
    ADD COLUMN Typ VARCHAR(30),
    ADD CHECK (Typ IN ('Standardbeställning', 'Specialbeställning'));

    UPDATE Bestallning SET Typ = 'Standardbeställning' WHERE BestallningID IN (1, 2, 4);
    UPDATE Bestallning SET Typ = 'Specialbeställning' WHERE BestallningID IN (3, 5);

    UPDATE Bestallning SET Typ = 'Standardbeställning' WHERE BestallningID IN (1, 2, 4);
    UPDATE Bestallning SET Typ = 'Specialbeställning' WHERE BestallningID IN (3, 5);


    ALTER TABLE orderitem MODIFY AnstalldID INT NULL;


    ALTER TABLE orderitem DROP FOREIGN KEY orderitem_ibfk_5;


    ALTER TABLE orderitem
    ADD CONSTRAINT orderitem_ibfk_5
    FOREIGN KEY (AnstalldID) REFERENCES anstalld(AnstalldID)
    ON DELETE SET NULL;

    UPDATE Kund SET Telefonnummer = '070-111-2233' WHERE KundID = 1;
    UPDATE Kund SET Telefonnummer = '073-998-8776' WHERE KundID = 2;
    UPDATE Kund SET Telefonnummer = '072-345-6789' WHERE KundID = 3;
    UPDATE Kund SET Telefonnummer = '076-554-4332' WHERE KundID = 4;
    UPDATE Kund SET Telefonnummer = '070-998-8776' WHERE KundID = 5;


    CREATE TABLE SpecialProdukt_Material (
        ID INT AUTO_INCREMENT PRIMARY KEY,
        SpecialProduktID INT,
        MaterialID INT,
        Antal INT,
        Beskrivning VARCHAR(255),
        FOREIGN KEY (SpecialProduktID) REFERENCES SpecialProdukt(SpecialProduktID),
        FOREIGN KEY (MaterialID) REFERENCES Material(MaterialID)
    );


    INSERT INTO SpecialProdukt (Storlek, Farg, Typ, Modell, Text, Dekoration, Pris, Beskrivning, Tillverkningstid, StatistikID)
    VALUES ('57', 'Svart/Röd', 'Skräddarsydd', 'Deluxe Cosplay', 'Epic look', 'Rävöron & fjädrar', 999, 'Specialdesignad cosplay-hatt', '14 dagar', 2);


    INSERT INTO SpecialProdukt_Material (SpecialProduktID, MaterialID, Antal, Beskrivning)
    VALUES
    (6, 2, 1, 'Basmaterial – Sammet vinröd'),
    (6, 5, 2, 'Dekoration – Rävöron'),
    (6, 4, 1, 'Innerfoder – Ninjatyg');


    CREATE TABLE StandardProdukt_Material (
        StandardProduktID INT,
        MaterialID INT,
        PRIMARY KEY (StandardProduktID, MaterialID),
        FOREIGN KEY (StandardProduktID) REFERENCES StandardProdukt(StandardProduktID),
        FOREIGN KEY (MaterialID) REFERENCES Material(MaterialID)
    );


    INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID) VALUES
    (1, 1), -- Studentmössa Klassisk 2025 använder Mösstyg
    (2, 3), -- Cosplay-hatt: Magiker använder Glitterband
    (4, 4), -- Cosplay-hatt: Ninja använder Ninjatyg
    (5, 3); -- Festhatt glitter använder Glitterband

    ALTER TABLE SpecialProdukt
    DROP COLUMN Farg,
    DROP COLUMN Typ,
    DROP COLUMN Modell,
    DROP COLUMN Dekoration;

    UPDATE StandardProdukt SET Storlek = 'XS' WHERE Storlek IN ('55', '56');
    UPDATE StandardProdukt SET Storlek = 'S'  WHERE Storlek IN ('57');
    UPDATE StandardProdukt SET Storlek = 'M'  WHERE Storlek IN ('58');
    UPDATE StandardProdukt SET Storlek = 'L'  WHERE Storlek IN ('59');
    UPDATE StandardProdukt SET Storlek = 'XL' WHERE Storlek IN ('60');

    UPDATE SpecialProdukt SET Storlek = 'XS' WHERE Storlek IN ('55', '56');
    UPDATE SpecialProdukt SET Storlek = 'S'  WHERE Storlek IN ('57');
    UPDATE SpecialProdukt SET Storlek = 'M'  WHERE Storlek IN ('58');
    UPDATE SpecialProdukt SET Storlek = 'L'  WHERE Storlek IN ('59');
    UPDATE SpecialProdukt SET Storlek = 'XL' WHERE Storlek IN ('60');

    ALTER TABLE Kund
    ADD COLUMN Postnummer VARCHAR(20),
    ADD COLUMN Land VARCHAR(100) DEFAULT 'Sverige';

    UPDATE Kund SET Postnummer = '114 22', Land = 'Sverige' WHERE KundID = 1; -- Stockholm
    UPDATE Kund SET Postnummer = '223 63', Land = 'Sverige' WHERE KundID = 2; -- Lund
    UPDATE Kund SET Postnummer = '753 18', Land = 'Sverige' WHERE KundID = 3; -- Uppsala
    UPDATE Kund SET Postnummer = '411 34', Land = 'Sverige' WHERE KundID = 4; -- Göteborg
    UPDATE Kund SET Postnummer = '214 21', Land = 'Sverige' WHERE KundID = 5; -- Malmö

    INSERT INTO Kund (Fornamn, Efternamn, Epost, Telefonnummer, Ort, LeveransAdress, FakturaAdress, StatistikID, Postnummer, Land)
    VALUES
    ('Mikko', 'Virtanen', 'mikko.virtanen@gmail.com', '+358401112233', 'Mariehamn', 'Havsgränden 1', 'Havsgränden 1', 2, '22100', 'Finland'),
    ('Emily', 'Clark', 'emily.clark@gmail.com', '+44 7911 123456', 'London', 'Baker Street 221B', 'Baker Street 221B', 4, 'W1A 1AA', 'Storbritannien'),
    ('James', 'Smith', 'james.smith@gmail.com', '+1 310-555-1212', 'Los Angeles', 'Sunset Blvd 500', 'Sunset Blvd 500', 3, '90210', 'USA');

    ALTER TABLE Kund
    ADD FakturaPostnummer VARCHAR(20),
    ADD FakturaOrt VARCHAR(100),
    ADD FakturaLand VARCHAR(100);

    -- Julia Sundström – Stockholm
    UPDATE Kund SET
        FakturaPostnummer = '114 22',
        FakturaOrt = 'Stockholm',
        FakturaLand = 'Sverige'
    WHERE KundID = 1;

    -- Felix Norberg – Lund
    UPDATE Kund SET
        FakturaPostnummer = '223 63',
        FakturaOrt = 'Lund',
        FakturaLand = 'Sverige'
    WHERE KundID = 2;

    -- Agnes Eriksson – Uppsala
    UPDATE Kund SET
        FakturaPostnummer = '753 18',
        FakturaOrt = 'Uppsala',
        FakturaLand = 'Sverige'
    WHERE KundID = 3;

    -- Viktor Karlsson – Göteborg
    UPDATE Kund SET
        FakturaPostnummer = '411 34',
        FakturaOrt = 'Göteborg',
        FakturaLand = 'Sverige'
    WHERE KundID = 4;

    -- Elin Blom – Malmö
    UPDATE Kund SET
        FakturaPostnummer = '214 21',
        FakturaOrt = 'Malmö',
        FakturaLand = 'Sverige'
    WHERE KundID = 5;

    -- Mikko Virtanen – Mariehamn (Åland)
    UPDATE Kund SET
        FakturaPostnummer = '22100',
        FakturaOrt = 'Mariehamn',
        FakturaLand = 'Finland'
    WHERE KundID = 6;

    -- Emily Clark – London
    UPDATE Kund SET
        FakturaPostnummer = 'W1A 1AA',
        FakturaOrt = 'London',
        FakturaLand = 'Storbritannien'
    WHERE KundID = 7;

    -- James Smith – Los Angeles
    UPDATE Kund SET
        FakturaPostnummer = '90210',
        FakturaOrt = 'Los Angeles',
        FakturaLand = 'USA'
    WHERE KundID = 8;

    ALTER TABLE Kund
    CHANGE COLUMN Postnummer LeveransPostnummer VARCHAR(20),
    CHANGE COLUMN Ort LeveransOrt VARCHAR(100),
    CHANGE COLUMN Land LeveransLand VARCHAR(100);

    ALTER TABLE Material
    DROP FOREIGN KEY Material_ibfk_1, -- Foreign key constraint StandardProduktID
    DROP FOREIGN KEY Material_ibfk_2, -- Foreign key constraint SpecialProduktID
    DROP COLUMN StandardProduktID,
    DROP COLUMN SpecialProduktID;

    -- ======================
    -- 1. MATERIAL
    -- ======================


    -- Lägg till förbättrade kolumner i Material
    ALTER TABLE Material
    ADD COLUMN Kategori VARCHAR(50),         -- Ex: "Stomme", "Dekoration"
    ADD COLUMN Anvandningsomrade VARCHAR(50);-- Ex: "Bas", "Dekoration", "Innerfoder"



    -- Exempeldata för material utifrån lärarens instruktioner
    INSERT INTO Material (Namn, Typ, Kategori, Farg, Pris) VALUES
    ('Ullfilt', 'Filt', 'Stomme', 'Naturvit', 35.00),
    ('Kaninfilt', 'Filt', 'Stomme', 'Svart', 50.00),
    ('Toquillastrå', 'Strå', 'Stomme', 'Natur', 45.00),
    ('Rishalm', 'Strå', 'Stomme', 'Natur', 20.00),
    ('Bomullstyg', 'Tyg', 'Stomme', 'Vit', 12.00),
    ('Läder', 'Läder', 'Stomme', 'Brun', 80.00),
    ('Påfågelfjädrar', 'Fjädrar', 'Dekoration', 'Flerfärgad', 15.00),
    ('Spets', 'Textil', 'Dekoration', 'Vit', 8.00),
    ('Fuskpäls', 'Textil', 'Dekoration', 'Grå', 18.00),
    ('Pärlor', 'Dekoration', 'Dekoration', 'Pärlemor', 22.00),
    ('Lurextråd', 'Tråd', 'Dekoration', 'Silver', 5.00);

    -- ======================
    -- 2. STOMME
    -- ======================

    -- Ny tabell: Stomme (med formtyp som attribut, ej egen tabell)
    CREATE TABLE IF NOT EXISTS Stomme (
        StommeID INT AUTO_INCREMENT PRIMARY KEY,
        Namn VARCHAR(100),
        Formtyp VARCHAR(50),         -- Ex: "Träform", "Metallform"
        Storlek VARCHAR(20),
        Pris DECIMAL(10,2)
    );

    -- Exempeldata för stommar
    INSERT INTO Stomme (Namn, Formtyp, Storlek, Pris) VALUES
    ('Viktoriansk stomme – Ullfilt', 'Träform', 'M', 150.00),
    ('Baseballkeps stomme – Bomull', 'Metallform', 'L', 90.00),
    ('Panamahatt stomme – Toquillastrå', 'Plastform', 'M', 130.00);

    -- ======================
    -- 3. KOPPLING: STOMME <-> MATERIAL
    -- ======================

    CREATE TABLE IF NOT EXISTS Stomme_Material (
        StommeID INT,
        MaterialID INT,
        Antal INT DEFAULT 1,
        PRIMARY KEY (StommeID, MaterialID),
        FOREIGN KEY (StommeID) REFERENCES Stomme(StommeID),
        FOREIGN KEY (MaterialID) REFERENCES Material(MaterialID)
    );

    -- Exempel på vilka material som används i stommar
    INSERT INTO Stomme_Material (StommeID, MaterialID, Antal) VALUES
    (1, 1, 2),  -- Viktoriansk stomme använder 2 st Ullfilt
    (2, 5, 1),  -- Keps använder 1 st Bomullstyg
    (3, 3, 2);  -- Panama använder 2 st Toquillastrå

    -- ======================
    -- 4. KOPPLING TILL PRODUKTER
    -- ======================

    ALTER TABLE StandardProdukt
    ADD COLUMN StommeID INT,
    ADD FOREIGN KEY (StommeID) REFERENCES Stomme(StommeID);

    ALTER TABLE SpecialProdukt
    ADD COLUMN StommeID INT,
    ADD FOREIGN KEY (StommeID) REFERENCES Stomme(StommeID);

    -- Exempel på att koppla produkter till stommar
    UPDATE StandardProdukt SET StommeID = 1 WHERE StandardProduktID = 1; -- Ex: Studentmössa Klassisk
    UPDATE StandardProdukt SET StommeID = 2 WHERE StandardProduktID = 2; -- Cosplay-hatt Magiker
    UPDATE SpecialProdukt SET StommeID = 1 WHERE SpecialProduktID = 1;   -- Viktoriansk Hög Hatt
    UPDATE SpecialProdukt SET StommeID = 3 WHERE SpecialProduktID = 3;   -- Kapten Deluxe

    -- ======================
    -- 5. BONUS – beskrivning i Material om användningsområde
    -- ======================

    UPDATE Material SET Anvandningsomrade = 'Bas' WHERE Kategori = 'Stomme';
    UPDATE Material SET Anvandningsomrade = 'Dekoration' WHERE Kategori = 'Dekoration';

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Filt Grå', 'Filt', 'Stomme', 'Bas', 'Grå', 38.5, NULL);

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Hampastrå', 'Strå', 'Stomme', 'Bas', 'Beige', 19.0, NULL);

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Tweed', 'Tyg', 'Stomme', 'Bas', 'Brunmelerad', 26.0, NULL);

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Tygblommor', 'Dekoration', 'Dekoration', 'Dekoration', 'Rosa', 12.0, NULL);

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Fjädrar från struts', 'Fjädrar', 'Dekoration', 'Dekoration', 'Svart', 17.5, NULL);

    INSERT INTO Material (Namn, Typ, Kategori, Anvandningsomrade, Farg, Pris, MaterialBestallningsID)
    VALUES ('Lackerat papper', 'Special', 'Dekoration', 'Dekoration', 'Metallicblå', 9.0, NULL);

    UPDATE Material
    SET Kategori = 'Stomme', Anvandningsomrade = 'Bas'
    WHERE MaterialID = 1;

    UPDATE Material
    SET Kategori = 'Stomme', Anvandningsomrade = 'Bas'
    WHERE MaterialID = 2;

    UPDATE Material
    SET Kategori = 'Dekoration', Anvandningsomrade = 'Dekoration'
    WHERE MaterialID = 3;

    UPDATE Material
    SET Kategori = 'Stomme', Anvandningsomrade = 'Bas'
    WHERE MaterialID = 4;

    UPDATE Material
    SET Kategori = 'Dekoration', Anvandningsomrade = 'Dekoration'
    WHERE MaterialID = 5;

    ALTER TABLE SpecialProdukt ADD COLUMN Matt INT;
    UPDATE SpecialProdukt SET Matt = 56 WHERE Storlek = 'XS';
    UPDATE SpecialProdukt SET Matt = 57 WHERE Storlek = 'S';
    UPDATE SpecialProdukt SET Matt = 58 WHERE Storlek = 'M';
    UPDATE SpecialProdukt SET Matt = 59 WHERE Storlek = 'L';
    UPDATE SpecialProdukt SET Matt = 60 WHERE Storlek = 'XL';

    ALTER TABLE StandardProdukt DROP COLUMN Storlek;
    ALTER TABLE SpecialProdukt DROP COLUMN Storlek;

    ALTER TABLE Kund ADD COLUMN Matt INT;
    UPDATE Kund SET Matt = 57 WHERE KundID = 1; -- Julia Sundström
    UPDATE Kund SET Matt = 58 WHERE KundID = 2; -- Felix Norberg
    UPDATE Kund SET Matt = 56 WHERE KundID = 3; -- Agnes Eriksson
    UPDATE Kund SET Matt = 59 WHERE KundID = 4; -- Viktor Karlsson
    UPDATE Kund SET Matt = 60 WHERE KundID = 5; -- Elin Blom
    UPDATE Kund SET Matt = 58 WHERE KundID = 6; -- Mikko Virtanen
    UPDATE Kund SET Matt = 59 WHERE KundID = 7; -- Emily Clark
    UPDATE Kund SET Matt = 57 WHERE KundID = 8; -- James Smith


    CREATE TABLE OrderItem_Material (
        ID INT AUTO_INCREMENT PRIMARY KEY,
        OrderItemID INT,
        MaterialID INT,
        Mängd DECIMAL(10,2),
        Enhet VARCHAR(20),         -- Exempel: 'cm', 'g', 'm', 'st'
        Beskrivning VARCHAR(255),  -- Valfritt: t.ex. "Basmaterial", "Dekoration"
        FOREIGN KEY (OrderItemID) REFERENCES OrderItem(OrderItemID),
        FOREIGN KEY (MaterialID) REFERENCES Material(MaterialID)
    );

    ALTER TABLE Material
    ADD COLUMN Enhet VARCHAR(10);

    ALTER TABLE Material
    MODIFY COLUMN Enhet VARCHAR(30);

    -- MaterialID 1–5
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 1;  -- Mösstyg
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 2;  -- Sammet
    UPDATE Material SET Enhet = 'Centimeter'       WHERE MaterialID = 3;  -- Glitterband
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 4;  -- Ninjatyg
    UPDATE Material SET Enhet = 'Styck'            WHERE MaterialID = 5;  -- Rävöron

    -- MaterialID 6–11
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 6;  -- Ullfilt
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 7;  -- Kaninfilt
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 8;  -- Toquillastrå
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 9;  -- Rishalm
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 10; -- Bomullstyg
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 11; -- Läder

    -- MaterialID 12–17
    UPDATE Material SET Enhet = 'Styck'            WHERE MaterialID = 12; -- Påfågelfjädrar
    UPDATE Material SET Enhet = 'Centimeter'       WHERE MaterialID = 13; -- Spets
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 14; -- Fuskpäls
    UPDATE Material SET Enhet = 'Styck'            WHERE MaterialID = 15; -- Pärlor
    UPDATE Material SET Enhet = 'Meter'            WHERE MaterialID = 16; -- Lurextråd
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 17; -- Filtgrå
    -- MaterialID 18–23
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 18; -- Filt Grå
    UPDATE Material SET Enhet = 'Decimeter'        WHERE MaterialID = 19; -- Hampastrå
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 20; -- Tweed
    UPDATE Material SET Enhet = 'Styck'            WHERE MaterialID = 21; -- Tygblommor
    UPDATE Material SET Enhet = 'Styck'            WHERE MaterialID = 22; -- Fjädrar från struts
    UPDATE Material SET Enhet = 'Kvadratdecimeter' WHERE MaterialID = 23; -- Lackerat papper

    -- 1. Ta bort tabellen OrderItem_Material om den finns
    DROP TABLE IF EXISTS OrderItem_Material;

    -- 2. Lägg till kolumner i StandardProdukt_Material
    ALTER TABLE StandardProdukt_Material
    ADD COLUMN Mängd DECIMAL(10,2),
    ADD COLUMN Enhet VARCHAR(20),
    ADD COLUMN Beskrivning VARCHAR(255);

    -- 3. Lägg till kolumner i SpecialProdukt_Material
    ALTER TABLE SpecialProdukt_Material
    ADD COLUMN Mängd DECIMAL(10,2),
    ADD COLUMN Enhet VARCHAR(20);

    -- === Uppdatera StandardProdukt_Material med mängd, enhet och beskrivning ===

    UPDATE StandardProdukt_Material
    SET Mängd = 8.0, Enhet = 'Decimeter', Beskrivning = 'Basmaterial – Mösstyg'
    WHERE StandardProduktID = 1 AND MaterialID = 1;

    UPDATE StandardProdukt_Material
    SET Mängd = 15.0, Enhet = 'Centimeter', Beskrivning = 'Dekoration – Glitterband'
    WHERE StandardProduktID = 2 AND MaterialID = 3;

    UPDATE StandardProdukt_Material
    SET Mängd = 9.0, Enhet = 'Decimeter', Beskrivning = 'Basmaterial – Ninjatyg'
    WHERE StandardProduktID = 4 AND MaterialID = 4;

    UPDATE StandardProdukt_Material
    SET Mängd = 12.0, Enhet = 'Centimeter', Beskrivning = 'Dekoration – Glitterband'
    WHERE StandardProduktID = 5 AND MaterialID = 3;


    -- === Uppdatera SpecialProdukt_Material med mängd och enhet (beskrivning finns redan) ===

    UPDATE SpecialProdukt_Material
    SET Mängd = 10.0, Enhet = 'Decimeter'
    WHERE SpecialProduktID = 6 AND MaterialID = 2;

    UPDATE SpecialProdukt_Material
    SET Mängd = 2.0, Enhet = 'Styck'
    WHERE SpecialProduktID = 6 AND MaterialID = 5;

    UPDATE SpecialProdukt_Material
    SET Mängd = 7.5, Enhet = 'Decimeter'
    WHERE SpecialProduktID = 6 AND MaterialID = 4;

    ALTER TABLE SpecialProdukt_Material
    DROP COLUMN Antal;

    ALTER TABLE SpecialProdukt_Material
    CHANGE COLUMN ID SpecMatID INT AUTO_INCREMENT;


    -- 1. Ta bort den gamla kopplingstabellen (om du vill bevara data, gör backup först!)
    DROP TABLE IF EXISTS StandardProdukt_Material;

    -- 2. Skapa om tabellen med unikt ID och fler attribut
    CREATE TABLE StandardProdukt_Material (
        StdMatID INT AUTO_INCREMENT PRIMARY KEY,
        StandardProduktID INT,
        MaterialID INT,
        Mängd DECIMAL(10,2),
        Enhet VARCHAR(30),
        Beskrivning VARCHAR(255),
        FOREIGN KEY (StandardProduktID) REFERENCES StandardProdukt(StandardProduktID),
        FOREIGN KEY (MaterialID) REFERENCES Material(MaterialID)
    );

    -- 3. Exempeldata med mängd och enhet (kan anpassas efter behov)
    INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID, Mängd, Enhet, Beskrivning) VALUES
    (1, 1, 5.0, 'Decimeter', 'Basmaterial – Mösstyg'),
    (2, 3, 30.0, 'Centimeter', 'Dekoration – Glitterband'),
    (4, 4, 6.0, 'Decimeter', 'Innerfoder – Ninjatyg'),
    (5, 3, 25.0, 'Centimeter', 'Dekoration – Glitterband');

    -- Lägg till flera standardprodukter till BestallningID = 1
    INSERT INTO OrderItem (AntalProdukter, BestallningID, StandardProduktID, SpecialProduktID, ProduktionsSchemaID, AnstalldID) VALUES
    (1, 1, 3, NULL, 1, 1),  -- Sjömansmössa
    (2, 1, 5, NULL, 1, 1);  -- Festhatt glitter (2 st)

    -- Ta bort Kategori och Anvandningsomrade från Material
    ALTER TABLE Material
    DROP COLUMN Kategori,
    DROP COLUMN Anvandningsomrade;
    ALTER TABLE Material
    ADD COLUMN Beskrivning VARCHAR(255);

    INSERT INTO Material (Namn, Typ, Farg, Pris, Enhet, Beskrivning) VALUES
    ('Linne', 'Tyg', 'Ljusbeige', 14.00, 'Decimeter', 'Lätt och luftigt tyg – vanligt till sommarhattar'),
    ('Silke', 'Tyg', 'Vit', 32.00, 'Decimeter', 'Glansigt och elegant tyg för brudhattar och fest'),
    ('Satin', 'Tyg', 'Champagne', 28.00, 'Decimeter', 'Glansigt tyg – används till band och dekorationer'),
    ('Polyester', 'Tyg', 'Svart', 9.00, 'Decimeter', 'Slitstarkt tyg – vanligt i sportiga hattar'),
    ('Palmblad', 'Strå', 'Naturgrön', 18.00, 'Decimeter', 'Används i handgjorda stråhattar'),
    ('Majsblad', 'Strå', 'Ljusgul', 16.00, 'Decimeter', 'Traditionellt stråmaterial för rustika hattar'),
    ('Hönsfjädrar', 'Fjädrar', 'Vit', 6.50, 'Styck', 'Små fjädrar – för dekoration'),
    ('Strutsfjädrar', 'Fjädrar', 'Svart', 17.50, 'Styck', 'Ger volym och dramatik – dekoration'),
    ('Tyll', 'Textil', 'Vit', 7.00, 'Decimeter', 'Lätt och genomskinligt tyg för slöjor och dekoration'),
    ('Organza', 'Textil', 'Rosa', 9.00, 'Decimeter', 'Tunt och skört tyg för romantiska hattar'),
    ('Linnesnöre', 'Dekoration', 'Natur', 3.50, 'Meter', 'Dekorband i naturmaterial för vintagekänsla');

    UPDATE Material SET Beskrivning = 'Tygmaterial – används till hattens ytter- eller innertyg'
    WHERE Beskrivning IS NULL AND Typ = 'Tyg';

    UPDATE Material SET Beskrivning = 'Filtmaterial – används i stommar för filthattar'
    WHERE Beskrivning IS NULL AND Typ = 'Filt';

    UPDATE Material SET Beskrivning = 'Stråmaterial – används för panamahattar och stråhattar'
    WHERE Beskrivning IS NULL AND Typ = 'Strå';

    UPDATE Material SET Beskrivning = 'Läder – används för robusta hattar med vintagekänsla'
    WHERE Beskrivning IS NULL AND Typ = 'Läder';

    UPDATE Material SET Beskrivning = 'Fjädrar – dekorativt material för stilfulla detaljer'
    WHERE Beskrivning IS NULL AND Typ = 'Fjädrar';

    UPDATE Material SET Beskrivning = 'Textil – används för dekorationer eller specialeffekter'
    WHERE Beskrivning IS NULL AND Typ = 'Textil';

    UPDATE Material SET Beskrivning = 'Dekoration – används för att förhöja hattens utseende'
    WHERE Beskrivning IS NULL AND Typ = 'Dekoration';

    UPDATE Material SET Beskrivning = 'Tråd – används för glittereffekter och finish'
    WHERE Beskrivning IS NULL AND Typ = 'Tråd';

    UPDATE Material SET Beskrivning = 'Specialmaterial – används för unika estetiska uttryck'
    WHERE Beskrivning IS NULL AND Typ = 'Special';

    ALTER TABLE StandardProdukt_Material
    ADD COLUMN Funktion VARCHAR(50);

    ALTER TABLE SpecialProdukt_Material
    ADD COLUMN Funktion VARCHAR(50);

    UPDATE StandardProdukt_Material
    SET Funktion = 'Basmaterial'
    WHERE StandardProduktID = 1 AND MaterialID = 1;

    UPDATE StandardProdukt_Material
    SET Funktion = 'Dekoration'
    WHERE StandardProduktID = 2 AND MaterialID = 3;

    UPDATE StandardProdukt_Material
    SET Funktion = 'Innerfoder'
    WHERE StandardProduktID = 4 AND MaterialID = 4;

    UPDATE StandardProdukt_Material
    SET Funktion = 'Dekoration'
    WHERE StandardProduktID = 5 AND MaterialID = 3;

    UPDATE SpecialProdukt_Material
    SET Funktion = 'Basmaterial'
    WHERE SpecialProduktID = 6 AND MaterialID = 2;

    UPDATE SpecialProdukt_Material
    SET Funktion = 'Dekoration'
    WHERE SpecialProduktID = 6 AND MaterialID = 5;

    UPDATE SpecialProdukt_Material
    SET Funktion = 'Innerfoder'
    WHERE SpecialProduktID = 6 AND MaterialID = 4;

    INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID, Mängd, Enhet, Beskrivning, Funktion)
    VALUES
    (1, 1, 8.0, 'Decimeter', 'Mösstyg – vit', 'Yttertyg'),
    (1, 4, 5.0, 'Decimeter', 'Ninjatyg – svart', 'Innertyg');

    INSERT INTO SpecialProdukt_Material (SpecialProduktID, MaterialID, Mängd, Enhet, Beskrivning, Funktion)
    VALUES
    (1, 2, 10.0, 'Decimeter', 'Sammet vinröd', 'Yttertyg'),
    (1, 4, 7.5, 'Decimeter', 'Ninjatyg – svart', 'Innertyg');

    ALTER TABLE StandardProdukt_Material DROP COLUMN Beskrivning;
    ALTER TABLE SpecialProdukt_Material DROP COLUMN Beskrivning;

    -- Ta bort kolumnen Enhet från StandardProdukt_Material
    ALTER TABLE StandardProdukt_Material
    DROP COLUMN Enhet;

    -- Ta bort kolumnen Enhet från SpecialProdukt_Material
    ALTER TABLE SpecialProdukt_Material
    DROP COLUMN Enhet;

    ALTER TABLE SpecialProdukt
    ADD COLUMN Hojd DECIMAL(5,2),
    ADD COLUMN Bredd DECIMAL(5,2),
    ADD COLUMN Djup DECIMAL(5,2);

    UPDATE orderitem
    SET AnstalldID = NULL;

ALTER TABLE StandardProdukt ADD COLUMN Aktiv BOOLEAN DEFAULT TRUE;

ALTER TABLE StandardProdukt
DROP COLUMN Dekoration;

-- Studentmössa – Sjömansmössa (StandardProduktID = 3)
INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID, Mängd, Funktion)
SELECT 3, m.MaterialID, 7.0, 'Basmaterial'
FROM Material m
WHERE m.Namn = 'Bomullstyg';

-- Festhatt glitter (StandardProduktID = 5) – har redan glitterband men kan få yttertyg också
INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID, Mängd, Funktion)
SELECT 5, m.MaterialID, 4.0, 'Basmaterial'
FROM Material m
WHERE m.Namn = 'Polyester';

INSERT INTO StandardProdukt (Namn, Modell, Typ, Farg, Text, Pris, StatistikID, Artikelnummer, Matt)
VALUES
('Studentmössa Klassisk 2025', 'SM-25-KLASS-BLÅ', 'Studentmössa', 'Vit/Blå', 'Lycka till!', 449, 1, '551100', 57),
('Studentmössa Klassisk 2025', 'SM-25-KLASS-GULD', 'Studentmössa', 'Vit/Guld', 'Vi ses i framtiden!', 449, 1, '551101', 57);

INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, FraktsedelID, TotalPris, Typ)
VALUES
('Levererad', '2025-04-15', FALSE, 1, 1, 898, 'Standardbeställning'),
('Levererad', '2025-04-16', FALSE, 2, 2, 1347, 'Standardbeställning');

-- test
-- Två beställningar av samma hatt (samma namn, olika artikelnummer/produktID)
INSERT INTO OrderItem (AntalProdukter, BestallningID, StandardProduktID, ProduktionsSchemaID, AnstalldID)
VALUES
(1, 6, 6, 1, 1),  -- Klassisk blå mössa
(2, 7, 7, 1, 2);  -- Klassisk guldmössa

-- === KUND ===
INSERT INTO Kund (Fornamn, Efternamn, Epost, Telefonnummer, LeveransOrt, LeveransAdress, FakturaAdress, StatistikID, LeveransPostnummer, LeveransLand, FakturaPostnummer, FakturaOrt, FakturaLand, Matt)
VALUES
('Anna', 'Nilsson', 'anna.nilsson@mail.se', '0701010101', 'Västerås', 'Gata 1', 'Gata 1', 1, '722 10', 'Sverige', '722 10', 'Västerås', 'Sverige', 57),
('Erik', 'Larsson', 'erik.larsson@mail.se', '0702020202', 'Örebro', 'Gata 2', 'Gata 2', 2, '703 60', 'Sverige', '703 60', 'Örebro', 'Sverige', 58),
('Sara', 'Johansson', 'sara.johansson@mail.se', '0703030303', 'Linköping', 'Gata 3', 'Gata 3', 3, '582 20', 'Sverige', '582 20', 'Linköping', 'Sverige', 59),
('Oskar', 'Lind', 'oskar.lind@mail.se', '0704040404', 'Helsingborg', 'Gata 4', 'Gata 4', 4, '252 20', 'Sverige', '252 20', 'Helsingborg', 'Sverige', 60),
('Emelie', 'Svensson', 'emelie.svensson@mail.se', '0705050505', 'Norrköping', 'Gata 5', 'Gata 5', 5, '602 10', 'Sverige', '602 10', 'Norrköping', 'Sverige', 57),
('Lucas', 'Berg', 'lucas.berg@mail.se', '0706060606', 'Luleå', 'Gata 6', 'Gata 6', 1, '972 32', 'Sverige', '972 32', 'Luleå', 'Sverige', 58),
('Tove', 'Holm', 'tove.holm@mail.se', '0707070707', 'Borås', 'Gata 7', 'Gata 7', 2, '504 30', 'Sverige', '504 30', 'Borås', 'Sverige', 59),
('Simon', 'Ek', 'simon.ek@mail.se', '0708080808', 'Gävle', 'Gata 8', 'Gata 8', 3, '802 50', 'Sverige', '802 50', 'Gävle', 'Sverige', 60),
('Nina', 'Bäck', 'nina.back@mail.se', '0709090909', 'Falun', 'Gata 9', 'Gata 9', 4, '791 31', 'Sverige', '791 31', 'Falun', 'Sverige', 57),
('Axel', 'Åsberg', 'axel.asberg@mail.se', '0700101010', 'Sundsvall', 'Gata 10', 'Gata 10', 5, '852 34', 'Sverige', '852 34', 'Sundsvall', 'Sverige', 58),
('Mira', 'Haglund', 'mira.haglund@mail.se', '0701111111', 'Karlstad', 'Gata 11', 'Gata 11', 1, '653 40', 'Sverige', '653 40', 'Karlstad', 'Sverige', 59),
('Jonas', 'Palm', 'jonas.palm@mail.se', '0701212121', 'Trollhättan', 'Gata 12', 'Gata 12', 2, '461 30', 'Sverige', '461 30', 'Trollhättan', 'Sverige', 60);

-- === MATERIAL ===
INSERT INTO Material (Namn, Typ, Farg, Pris, Enhet, Beskrivning)
VALUES
('Filt Svart', 'Filt', 'Svart', 36.0, 'Kvadratdecimeter', 'Mörkt filtmaterial för eleganta hattar'),
('Bomull Blå', 'Tyg', 'Blå', 10.5, 'Decimeter', 'Bomullstyg för klassiska kepsar'),
('Fjäder Blå', 'Fjädrar', 'Blå', 13.0, 'Styck', 'För dekoration'),
('Dekorband Röd', 'Dekoration', 'Röd', 6.0, 'Meter', 'Används som dekorband'),
('Ståltråd', 'Special', 'Silver', 3.0, 'Meter', 'Används i stomkonstruktion'),
('Pärlor Guld', 'Dekoration', 'Guld', 22.0, 'Styck', 'Lyxig pärldekoration'),
('Tyll Svart', 'Textil', 'Svart', 6.0, 'Decimeter', 'Används för slöjor'),
('Organza Lila', 'Textil', 'Lila', 8.5, 'Decimeter', 'För cosplay och effekter'),
('Sammet Svart', 'Tyg', 'Svart', 30.0, 'Decimeter', 'Lyxig textil till eleganta hattar'),
('Strutsfjäder Vit', 'Fjädrar', 'Vit', 15.0, 'Styck', 'Dekorativ fjäder för showhattar');


-- ====== FLER KUNDER ======
INSERT INTO Kund (Fornamn, Efternamn, Epost, Telefonnummer, LeveransOrt, LeveransAdress, FakturaAdress, StatistikID, LeveransPostnummer, LeveransLand, FakturaPostnummer, FakturaOrt, FakturaLand, Matt)
VALUES
('Anna', 'Berg', 'anna.berg@example.com', '070-1122334', 'Örebro', 'Slottsgatan 1', 'Slottsgatan 1', 1, '702 11', 'Sverige', '702 11', 'Örebro', 'Sverige', 58),
('Lars', 'Johansson', 'lars.j@example.com', '070-2233445', 'Västerås', 'Hattgatan 12', 'Hattgatan 12', 2, '721 30', 'Sverige', '721 30', 'Västerås', 'Sverige', 57),
('Eva', 'Nilsson', 'eva.nilsson@example.com', '070-3344556', 'Norrköping', 'Nygatan 45', 'Nygatan 45', 3, '602 21', 'Sverige', '602 21', 'Norrköping', 'Sverige', 59);

-- ====== FLER FRAKTSEDELAR ======
INSERT INTO Fraktsedel (Adress, Avsandare, Mottagare, Innehåll, Exportkod, Pris, Datum, Vikt, Moms, PrisInklMoms)
VALUES
('Slottsgatan 1, Örebro', 'Hattmakarna AB', 'Anna Berg', 'Studentmössa', 'HATTSTUD2025', 89, '2025-04-17', 0.4, 22.25, 111.25),
('Hattgatan 12, Västerås', 'Hattmakarna AB', 'Lars Johansson', 'Cosplay Ninja', 'HATTNIN2025', 95, '2025-04-18', 0.5, 23.75, 118.75),
('Nygatan 45, Norrköping', 'Hattmakarna AB', 'Eva Nilsson', 'Festhatt glitter', 'HATTGLIT2025', 79, '2025-04-19', 0.3, 19.75, 98.75);

-- ====== FLER BESTÄLLNINGAR ======
INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, FraktsedelID, TotalPris, Typ)
VALUES
('Packas', '2025-04-17', TRUE, 9, 6, 449, 'Standardbeställning'),
('Produktion pågår', '2025-04-18', FALSE, 10, 7, 489, 'Standardbeställning'),
('Under behandling', '2025-04-19', TRUE, 11, 8, 129, 'Standardbeställning');

-- ====== FLER ORDERITEMS ======
INSERT INTO OrderItem (AntalProdukter, BestallningID, StandardProduktID, ProduktionsSchemaID, AnstalldID)
VALUES
(1, 8, 1, 1, 1),
(2, 9, 4, 2, 2),
(1, 10, 5, 3, NULL);

-- ====== FLER SPECIALPRODUKTER ======
INSERT INTO SpecialProdukt (Text, Pris, Beskrivning, Tillverkningstid, StatistikID, Matt, Hojd, Bredd, Djup, StommeID)
VALUES
('Gläns i natten!', 1050, 'Svart topphatt med glittereffekt', '9 dagar', 3, 57, 18.5, 22.0, 26.5, 1),
('Stolt som en tupp!', 980, 'Röd hög hatt med fjädrar', '8 dagar', 2, 58, 20.0, 23.0, 27.0, 2);

-- ====== FLER PRODUKTIONSSCHEMAN ======
INSERT INTO ProduktionsSchema (Namn) VALUES
('Helgschema'),
('Sommarteam');

-- ====== FLER MATERIALBESTÄLLNINGAR ======
INSERT INTO MaterialBestallning (Namn, Antal, BestallningsDatum) VALUES
('Extra glitterband', 100, '2025-04-01'),
('Sommarstrå', 200, '2025-04-02');

-- ====== FLER MATERIAL ======
INSERT INTO Material (Namn, Typ, Farg, Pris, Enhet, Beskrivning)
VALUES
('Sidenband', 'Textil', 'Rött', 6.5, 'Meter', 'Används för dekorativa inslag på högtidshattar'),
('Bomullsvadd', 'Textil', 'Vit', 4.0, 'Decimeter', 'Fyllning för komfort'),
('Skimrande tyg', 'Tyg', 'Silver', 15.0, 'Decimeter', 'Används i galahattar');

-- ====== FLER ANSTÄLLDA ======
INSERT INTO Anstalld (Fornamn, Efternamn, Losenord, Epost, Behorighet, ProduktionsSchemaID)
VALUES
('Eva', 'Söm', 'evasom123', 'eva@hattmakarna.se', 1, 1),
('Tom', 'Syfast', 'tompass', 'tom@hattmakarna.se', 1, 2);

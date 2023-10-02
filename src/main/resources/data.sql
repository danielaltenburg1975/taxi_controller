INSERT INTO "CARS" VALUES
('66241aed-2891-4da5-8647-a75ed1723709','offen', 'offen','offen','offen','offen', 'kein', 'kein'),
('72b11cec-0080-496a-a3a9-92494007befb', 'VW Bus T6', '08.2024','M-F-525','W 225/70 R16 || S 225/70 R16 ','8', '1235464/1212', '12/2024'),
('5ce3bbc1-6561-4c71-aa33-382692322b40', 'VW Bus T6', '11.2023','ZG-BK-525','W 225/70 R16 || S 225/70 R16', '8', '1235464/1212', '10/2024'),
('d6c3fe4e-e7c9-4961-b7fe-9350242992e9', 'VW Caddy','03.2024','M-ZG-525','W 225/70 R16 || S 225/70 R16','6', '1235464/1212', 'kein'),
('983b41a6-f1b6-4309-87df-5bfd2048392f', 'VW Caddy', '05.2025','M-SK-525','W 225/70 R16 || S 225/70 R16','6', '1235464/1212', '10/2024'),
('b501cfdc-ea0b-4659-b6d5-0449467a9ca0', 'VW T-Roc','05.2025','M-MK-525','W 225/70 R16 || S 225/70 R16','4', '1235464/1212', 'kein');



INSERT INTO "CUSTOMERS" VALUES
('c58f8dd9-95dc-4d01-836b-4df6b37de527','kein','kein','Taxibetrieb','kein','kein'),
('48767ba4-a9a2-4c0b-9ce9-cf0c53e8e45e', 'Ringstrasse14 78976 Friedheim','danielaltenburg@email.de','Daniel Altenburg', 'IKK' ,'0876 677854'),
('0f2c88b4-4f8f-4986-b7de-a03445ca8a18', 'Lingustenweg 3a 32984 Oberstadt', 'meyersigi@muster.de', 'Siegfried Meyer', 'AOK', '0876 677854'),
('ec8e18d7-fb99-493d-92d9-e6d31329fd74', 'Dalienweg 23 87654 Jested', 'kromerheinz@muster.de', 'Heinz Kromer', 'Techniker', '08976 989'),
('0ea74725-7bee-4a86-9ab0-89ac3c51a172', 'Im Hirschbühl 34 64532 Aichenberg', 'isanellehoenig@muster.de', 'Isabelle Hönig', 'IKK', '189 675634'),
('54852d85-c1f1-4cd6-bb8d-8631cf9013ee', 'Klosterweg 1 78987 Fahrnau', 'heinerebecca@web.lo', 'Rebecca Heine', 'Privat', '+49 176 4536789');


INSERT INTO "EMPLOYEES" VALUES
('8cf87de5-574d-44ab-bf7f-25dec35cca73','kein','kein','kein','kein','kein', 'kein','kein', '0000','kein','kein','kein'),
('51df6789-1b4f-4e32-b075-ef99e86ad9a5','Carlottenweg 8a 67543 Grünwald','01.07.2015-xxxx','heinzmeier@muster.de','23.07.1967','KV-Eintrag','Heinz Meier','80%','1212', 'SV-Eintrag', '1876 67453', '30'),
('e27a9ccf-a06c-44e1-9191-38983048b0f7', 'Haus am See 2 65476 Graubitten','15.01,2012-xxxx','berndbroedner@muster.de','25.03.1999','KV-Eintrag','Bernd Brödner','450€','1214', 'SV-Eintrag','49 189 3425643','0');

INSERT INTO "NEW_TRIP" VALUES
('2fb669ec-0659-49fb-af9f-e6f71ce8156f','Ringstrasse 14 78976 Friedheim','keine', '','', 'NEIN', '26.10.23, 12:00', 'Freiburg Uni','48767ba4-a9a2-4c0b-9ce9-cf0c53e8e45e','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('e313ccbd-be17-427a-be2f-f3a72f0be5bf','Ringstrasse 14 78976 Friedheim','keine', '','', 'NEIN', '28.10.23, 12:00', 'Freiburg Uni','48767ba4-a9a2-4c0b-9ce9-cf0c53e8e45e','51df6789-1b4f-4e32-b075-ef99e86ad9a5');

INSERT INTO "TIME_BOOKING" VALUES
('8db2356e-c5a4-461d-b231-6e13d4b48757', 'NEIN', '', '22.06.23, 08:00','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('5607291e-9497-4292-a180-12b835998f19', 'NEIN', '', '22.06.23, U','e27a9ccf-a06c-44e1-9191-38983048b0f7'),
('1333d198-909d-4e39-8ee7-b20411501c4e', 'NEIN', '', '22.06.23, 17:20','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('62a844c6-38b0-4ff1-9abe-af84f84dd67d', 'NEIN', '', '23.06.23, K','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('6e5b25a7-6bba-4ad1-946a-a69144eba12c', 'NEIN', '', '23.06.23, 08:10','e27a9ccf-a06c-44e1-9191-38983048b0f7'),
('65209521-8a54-4e62-981c-62e450edc275', 'NEIN', '', '23.06.23, 17:18','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('813867b8-dd0a-4783-9e85-34b766f20e50', 'NEIN', '', '23.06.23, 16:23','e27a9ccf-a06c-44e1-9191-38983048b0f7'),
('569e4f4f-a1b1-435f-9a51-11574efc4b14', 'NEIN', '', '24.06.23, 07:50','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('e586992e-ddb6-4b68-bb82-10eaee89f803', 'NEIN', '', '24.06.23, 08:05','e27a9ccf-a06c-44e1-9191-38983048b0f7'),
('9f021b62-a1f8-4795-b1e1-8ca09e163f1f', 'NEIN', '', '10.06.23, 17:35','e27a9ccf-a06c-44e1-9191-38983048b0f7');

INSERT INTO "TRIPCOLLECTOR" VALUES
('c58f8dd9-95dc-4d01-836b-4df6b37de527', 'Ringstrasse 14 78976 Friedheim','07764 78564  mit Begleitperson','','KTSs','','2','23.09.23, 23:00','Uni Freiburg','72b11cec-0080-496a-a3a9-92494007befb','48767ba4-a9a2-4c0b-9ce9-cf0c53e8e45e','51df6789-1b4f-4e32-b075-ef99e86ad9a5' ),
('66241aed-2891-4da5-8647-a75ed1723709', 'Lingustenweg 3a 32984 Oberstadt','0876 677854','', 'KTSs','','1', '23.09.23, 13:00', 'Uni Freiburg', 'd6c3fe4e-e7c9-4961-b7fe-9350242992e9','0f2c88b4-4f8f-4986-b7de-a03445ca8a18','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('95af9d1d-83a6-4a79-bb64-f9fea6b43835', 'Dalienweg 23 87654 Jested', '08976 989 ambulant','','BG Stahl', '','1','23.09.23, 08:00','Uni Freiburg','b501cfdc-ea0b-4659-b6d5-0449467a9ca0','ec8e18d7-fb99-493d-92d9-e6d31329fd74','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('c870120c-5f25-4a57-992b-d265cfc4c831', 'Im Hirschbühl 34 64532 Aichenberg ', '189 675634 Bestahlung', '','KTSs','','1', '12.10.23, 12:50','Klinik Singen','b501cfdc-ea0b-4659-b6d5-0449467a9ca0','0ea74725-7bee-4a86-9ab0-89ac3c51a172','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('28cc1d7e-07ee-4708-8b88-acb66884c1a1', 'Klosterweg 1 78987 Fahrnau','+49 176 4536789  mit 2x Sitzerhöhung 150€ bar','','bar','','6','12.10.23, 09:00','FLH Zürich','72b11cec-0080-496a-a3a9-92494007befb','54852d85-c1f1-4cd6-bb8d-8631cf9013ee','51df6789-1b4f-4e32-b075-ef99e86ad9a5');

INSERT INTO "TIME_ACCOUNT" VALUES
('e213341c-7ac6-4046-b6d6-9dd1291c9610','04.23','9.8 0 0 U/2 U U 0 8.0 9.2 U 0 7.5 5.0 6.8 4.2 0 0 8.2 K K 12.3 3.5 7.9 9.0 10.6 2.4 9.4 8.0 7.8 8.9 ','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('f32fd3b7-7656-4724-b881-e684fd9d944f','05.23','8.2 12.5 7.5 U U U 5.2 8.0 9.2 7.8 8.4 7.5 5.0 6.8 4.2 3.3 9.0 8.2 K K 12.3 3.5 7.9 9.0 10.6 2.4 9.4 8.0 7.8 8.9 K ','51df6789-1b4f-4e32-b075-ef99e86ad9a5'),
('78ad4f94-9c94-44de-83ca-611e92fbdd0a','05.23','8.2 12.5 7.5 U U U 5.2 8.0 9.2 7.8 8.4 7.5 5.0 6.8 4.2 3.3 9.0 8.2 K K 12.3 3.5 7.9 9.0 10.6 2.4 9.4 8.0 7.8 8.9 K ','e27a9ccf-a06c-44e1-9191-38983048b0f7');


INSERT INTO "MEMORIES" VALUES
('5607291e-9497-4292-a180-12b835998f19','23.09.23', 'Hier könnte eine Erinnerung stehen. Wie z.B. Morgen 14:00 Uhr Reifenwechseln M-F-525 Fa.Mustermann.');






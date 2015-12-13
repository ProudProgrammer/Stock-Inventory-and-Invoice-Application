# Stock Inventory and Invoice Application
### (Java Swing, JDBC, MySQL, Socket Mechanism, Executor Framework, java.awt.print, UML diagrams)
This is a 3-layer application: fat client, server, DBMS. This was my thesis at the university. I was about to make it in enterprise environment with Java EE but I changed my mind because I thought I would make everything myself such as connection between client and server, thread management, transaction managemant (with connection.setAutoCommit(false) and connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ)), object-relation mapping (with JDBC ResultSet and own entity classes). This app is multi-user and it has a very user-friendly GUI. With this project, I learnt a lot about software engineering, database planning, testing, debugging, GUI designing, making documents about the product.
## In Hungarian
### Raktárkezelő- és számlavezető alkalmazás
Ez egy háromrétegű alkalmazás: vastag kliens, szerver, DBMS. Ez volt a szakdolgozatom az egyetemen. Úgy volt, hogy nagyvállalati környezetben készítem el Java EE-vel, de meggondoltam magam mert úgy éreztem, hogy inkább magam valósítok meg mindent, úgy mint a kapcsolat a kliens és a szerver között, szálkezelés, objektum-relációs leképezés (JDBC ResultSet és saját entitás osztályok alkalmazásával), tranzakciókezelés (ezt rém egyszerű módon a connection.setAutoCommit(false) és connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ) beállításokkal). Természetesen keretrendszereket kell ezekre a dolgokra használni, de tanulási célra és szakdolgozathoz hasznos volt ezeket magamnak megvalósítani. Az alkalmazás többfelhasználós, a GUI felhasználóbarát. Ezzel a projekttel sokat tanultam a szoftvertervezésről, adatbázis tervezésről, tesztelésről, hibakeresésről, felhasználói felület tervezésről, dokumentálásról.

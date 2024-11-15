## Vorbereitung

Um den Code auszuführen, benötigen Sie eine PostgreSQL-Datenbank auf Ihrem lokalen Rechner. Sie können diese [hier](https://www.postgresql.org/download/) herunterladen.

### Datenbank erstellen

Erstellen Sie eine Datenbank mit dem Namen `geraeteausleihe`. Sie können dies im SQL-Shell mit dem Befehl `create database geraeteausleihe` tun, sobald Sie eingeloggt sind.

### Datenbankverbindung

Wir verwenden die lokalen Datenbankkontoinformationen des Standardbenutzers `postgres` mit dem Passwort `admin1`. Wenn Sie ein anderes Passwort haben oder einen anderen Benutzer verwenden möchten, können Sie die Informationen in der Datei `/src/main/resources/application.properties` ändern.


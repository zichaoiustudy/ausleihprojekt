## Vorbereitung

Um den Code auszuführen, benötigen Sie eine PostgreSQL-Datenbank auf Ihrem lokalen Rechner. Sie können diese [hier](https://www.postgresql.org/download/) herunterladen.

### Datenbank erstellen

Erstellen Sie eine Datenbank mit dem Namen `geraeteausleihe`. Sie können dies im SQL-Shell mit dem Befehl `create database geraeteausleihe` tun, sobald Sie eingeloggt sind.

![SQL Shell Terminal](https://github.com/user-attachments/assets/263f618a-eb29-4cad-afda-f089db036550)

### Datenbankverbindung

Wir verwenden die lokalen Datenbankkontoinformationen des Standardbenutzers `postgres` mit dem Passwort `admin1`. Wenn Sie ein anderes Passwort haben oder einen anderen Benutzer verwenden möchten, können Sie die Informationen in der Datei `/src/main/resources/application.properties` ändern.

# Geräteausleihe Mini-Web-App

## Überblick

Wenn der Benutzer die Seite der Mini-Web-App öffnet, wird er mit der Begrüßung **"Willkommen bei der Geräteausleihe"** und einem passenden Hintergrund empfangen. Unmittelbar darunter befindet sich ein **Suchfeld**, in das der Benutzer eingeben kann, nach welchem Gerät er gerade sucht. In der oberen rechten Ecke ist außerdem eine **Admin-Schaltfläche** vorhanden.

![img](https://github.com/user-attachments/assets/c01847b7-d536-4638-b4ee-0c3c7b0dc07b)

## Geräteverwaltung (Admin)

Unter dem Admin-Punkt wird eine **Geräteliste** angezeigt, die verschiedene Geräte zum Ausleihen auflistet. Jedes Gerät enthält die folgenden Informationen:

- **ID**
- **Gerätetyp**
- **Modell**
- **Maximale Ausleihdauer** (in Tagen)
- **Aktueller Status**

Der **Admin** kann diese Geräte verwalten und neue Geräte hinzufügen. Zusätzlich gibt es eine **Suchfunktion**, die es ermöglicht, die Geräte gezielt zu filtern.

![img_3](https://github.com/user-attachments/assets/3b8ca3af-e4ba-419e-90ff-401cae403835)

## Navigation

In der linken oberen Ecke kann zwischen zwei Ansichten gewechselt werden:

- **Geräteliste**
- **Buchungsübersicht**

![img_4](https://github.com/user-attachments/assets/f5e0f2d9-c3c1-45e4-9a94-7487c025156f)

## Buchungsübersicht

Auf der **Buchungsseite** wird angezeigt, wer welches Gerät ausgeliehen hat und wie lange es gebucht ist.

![img_5](https://github.com/user-attachments/assets/89bb78d7-c02e-4e83-b610-d5091cc840f4)

## Geräte suchen und buchen

Wenn der Benutzer nun ein **Gerät sucht**, sieht er alle verfügbaren Geräte sowie deren aktuellen Status. Nachdem ein Gerät ausgewählt wurde, sieht der Benutzer:

- Eine **Kalenderansicht**
- Die **Eigenschaften des Geräts**

Der Benutzer kann auf ein bestimmtes Datum im Kalender klicken, um das gewünschte **Ausleihdatum** festzulegen.

![img_7](https://github.com/user-attachments/assets/0adcc931-66da-4a48-8cae-fff48123d7e6)

## Gerät buchen mit Hilfe einer Kalenderintegration

Nachdem der Benutzer ein Datum ausgewählt hat, hat er die Möglichkeit, das Gerät zu buchen, indem er seinen **Namen** angibt. Zusätzlich wird die **Buchungshistorie** des Geräts angezeigt und der Status auf **"vermietet"** gesetzt.

![img_8](https://github.com/user-attachments/assets/d24c7ecc-29fa-49a2-90d5-20afed7a16d0)

Der erste Tag der Ausleihe wird im Kalender gelb markiert und die Tage für die **maximale Ausleihdauer** des Gerätes werden rot hervorgehoben. 

![Maximale Ausleihdauer](https://github.com/user-attachments/assets/23a1ffcf-7047-4535-9727-4de830c4b58e)

Im Kalender wird nach Abgabe des Gerätes die Buchung in der Buchungshistorie vervollständigt und der Status des Gerätes wird auf **"verfügbar"** geändert. Der **Zeitraum** der vollendeten Buchung wird auch im Kalender markiert.

![Zeitraum](https://github.com/user-attachments/assets/d8331e93-29ee-410b-a924-1ff933d4ba41)

Der Benutzer kann auch auf die **vorherige Ansicht** zurückkehren.

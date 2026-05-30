# Spezifikation Admin-Use-Cases Frontend

## Zweck

Diese Spezifikation beschreibt die Frontend-Anforderungen fuer den Adminbereich des Zauberbedarf Online Shops. Sie konkretisiert die Use Cases `UC-10` bis `UC-12` aus `features/overview.md` fuer die Bedienoberflaeche.

## Geltungsbereich

Der Frontend-Teil umfasst:

- Sichtbarkeit und Zugang zum Adminbereich
- Verwaltungsoberflaechen fuer Produkte und Kategorien
- Formulare, Tabellen, Filter, Bestaetigungen und Fehlermeldungen

Nicht enthalten sind:

- Designsystem im Detail
- Drag-and-drop-Sortierung
- Bulk-Aktionen auf mehrere Datensaetze gleichzeitig

## Navigation und Zugriff

- Ein Admin sieht nach dem Login einen Einstieg in den Adminbereich.
- Nicht-Admins sehen diesen Einstieg nicht.
- Direkte Aufrufe geschuetzter Admin-Seiten fuehren fuer Nicht-Admins zu einer Zugriffsfehlermeldung oder Umleitung.

## Use Case UC-10: Adminbereich oeffnen

### Ziel

Der Admin erreicht die Verwaltungsoberflaeche schnell und eindeutig.

### Frontend-Anforderungen

- Es gibt einen klaren Navigationspunkt wie `Admin`, `Verwaltung` oder `Dashboard`.
- Die Startseite des Adminbereichs zeigt mindestens die Bereiche `Produkte` und `Kategorien`.
- Die Admin-Startseite darf Kennzahlen oder eine einfache Uebersicht enthalten, muss aber mindestens als Verteiler zu den Verwaltungslisten funktionieren.

### Akzeptanzkriterien

- Ein Admin kann den Adminbereich direkt nach dem Login oeffnen.
- Ein Nicht-Admin kann die Seite nicht erfolgreich nutzen.

## Use Case UC-11: Produkte verwalten

### Ziel

Admins koennen Produkte effizient anlegen und pflegen.

### Listenansicht

Die Produktliste zeigt mindestens:

- Name
- Preis
- Lagerbestand
- Kategorien
- Aktiv-Status
- Bearbeitungsaktion

Die Liste soll mindestens unterstuetzen:

- Suche nach Name
- Filter nach Kategorie
- Filter nach Aktiv-Status
- Aktion zum Anlegen eines neuen Produkts

### Formular fuer Anlegen und Bearbeiten

Das Produktformular enthaelt mindestens:

- Name
- Beschreibung
- Preis
- Lagerbestand
- Kategorieauswahl
- Bild-Referenz oder Bild-URL
- Aktiv-Status

### Interaktionsregeln

- Pflichtfelder werden im Formular gekennzeichnet.
- Validierungsfehler werden feldnah angezeigt.
- Erfolgreiches Speichern wird klar bestaetigt.
- Vor dem Loeschen wird eine Bestaetigung eingeholt.

### Zustandsdarstellung

- Ladezustaende werden sichtbar gemacht.
- Leere Listen werden mit einer hilfreichen Meldung dargestellt.
- API-Fehler werden fuer Admins verstaendlich angezeigt.

### Akzeptanzkriterien

- Ein Admin kann ein Produkt aus der Liste heraus bearbeiten.
- Ein Admin kann ein neues Produkt ueber ein Formular anlegen.
- Ein Admin kann ein Produkt deaktivieren, ohne es zwingend loeschen zu muessen.

## Use Case UC-12: Kategorien verwalten

### Ziel

Admins koennen die Kategoriestruktur uebersichtlich pflegen.

### Listenansicht

Die Kategorienliste zeigt mindestens:

- Name
- Beschreibung
- Aktiv-Status
- Anzahl zugeordneter Produkte, falls verfuegbar
- Bearbeitungsaktion

Die Liste soll mindestens unterstuetzen:

- Aktion zum Anlegen einer neuen Kategorie
- Bearbeiten bestehender Kategorien
- Aktivieren oder Deaktivieren von Kategorien

### Formular fuer Anlegen und Bearbeiten

Das Kategorieformular enthaelt mindestens:

- Name
- Beschreibung
- Aktiv-Status

### Interaktionsregeln

- Vor dem Loeschen wird eine Bestaetigung eingeholt.
- Falls eine Kategorie wegen bestehender Produktzuordnungen nicht geloescht werden kann, zeigt das Frontend die Backend-Fehlermeldung verstaendlich an.

### Akzeptanzkriterien

- Ein Admin kann eine Kategorie anlegen und bearbeiten.
- Eine fehlgeschlagene Loeschung wegen bestehender Produktzuordnungen wird nachvollziehbar angezeigt.

## UX-Anforderungen

- Der Adminbereich soll auf Desktop mindestens tabellarische Verwaltungsansichten unterstuetzen.
- Auf kleineren Bildschirmen sollen Tabellen weiterhin nutzbar bleiben, zum Beispiel durch umbrechende Zeilen oder horizontales Scrollen.
- Formulare sollen ohne unnoetige Zwischenschritte bedienbar sein.

## Status- und Fehlermeldungen

- Erfolgreiche Schreiboperationen zeigen eine sichtbare Rueckmeldung.
- Fehler durch fehlende Berechtigung werden klar von Validierungsfehlern unterschieden.
- Netzwerk- oder Serverfehler werden als allgemeine Fehlermeldung angezeigt, ohne technische Interna vorauszusetzen.

## Frontend-Datenmodell

Das Frontend benoetigt fuer die Verwaltungsansichten mindestens folgende Daten:

- Produkt: `id`, `name`, `beschreibung`, `preis`, `lagerbestand`, `kategorien`, `bildUrl`, `aktiv`
- Kategorie: `id`, `name`, `beschreibung`, `aktiv`
- Benutzerkontext: `rolle`

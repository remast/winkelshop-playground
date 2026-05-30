# Spezifikation Admin-Use-Cases Backend

## Zweck

Diese Spezifikation beschreibt die Backend-Anforderungen fuer den Adminbereich des Zauberbedarf Online Shops. Sie konkretisiert die Use Cases `UC-10` bis `UC-12` aus `features/overview.md` fuer die serverseitige Umsetzung.

## Geltungsbereich

Der Backend-Teil umfasst:

- Zugriffsschutz fuer Admin-Funktionen
- Lesen, Anlegen, Aendern, Aktivieren/Deaktivieren und Loeschen von Produkten
- Lesen, Anlegen, Aendern, Aktivieren/Deaktivieren und Loeschen von Kategorien
- Validierung, Fehlerfaelle und Auswirkungen auf den sichtbaren Shop-Katalog

Nicht enthalten sind:

- Erweiterte Rollenmodelle
- Medienverwaltung ausser einer Bild-Referenz am Produkt
- Massenimporte oder Exporte

## Rollen und Berechtigungen

- Nur Benutzer mit Rolle `Admin` duerfen auf Admin-Endpunkte zugreifen.
- Nicht eingeloggte Aufrufe werden mit `401 Unauthorized` abgelehnt.
- Eingeloggte Benutzer ohne Admin-Rolle werden mit `403 Forbidden` abgelehnt.
- Admins verwenden dieselbe Anmeldung wie Kaeufer; die Rolle wird serverseitig ausgewertet.

## Domaenenobjekte

### Produkt

Pflichtattribute:

- `id`
- `name`
- `beschreibung`
- `preis`
- `lagerbestand`
- mindestens eine Kategoriezuordnung
- `aktiv`

Optionale Attribute:

- `bildUrl` oder eine vergleichbare Bild-Referenz

Regeln:

- `name` ist nicht leer.
- `preis` ist groesser oder gleich `0`.
- `lagerbestand` ist eine ganze Zahl groesser oder gleich `0`.
- Zugeordnete Kategorien muessen existieren.

### Kategorie

Pflichtattribute:

- `id`
- `name`
- `beschreibung`
- `aktiv`

Regeln:

- `name` ist nicht leer.
- Kategorienamen sollen eindeutig sein.

## Use Case UC-10: Adminbereich oeffnen

### Ziel

Der Admin kann einen geschuetzten Verwaltungsbereich verwenden.

### Backend-Anforderungen

- Das Backend stellt mindestens einen geschuetzten Admin-Einstiegspunkt bereit, ueber den Produkte und Kategorien geladen werden koennen.
- Nach erfolgreicher Authentifizierung kann das Frontend die Rolle des eingeloggten Benutzers abfragen.
- Das Backend liefert fuer Admins die fuer die Verwaltung benoetigten Listenendpunkte.

### Akzeptanzkriterien

- Ein Admin kann Admin-Daten abrufen.
- Ein Kaeufer erhaelt keinen Zugriff auf Admin-Daten.

## Use Case UC-11: Produkte verwalten

### Ziel

Admins koennen das Sortiment pflegen.

### Erforderliche Backend-Funktionen

- Produktliste abrufen
- Produktdetails abrufen
- Produkt anlegen
- Produkt bearbeiten
- Produkt aktivieren oder deaktivieren
- Produkt loeschen

### Empfohlene Endpunkte

- `GET /api/admin/products`
- `GET /api/admin/products/:id`
- `POST /api/admin/products`
- `PUT /api/admin/products/:id`
- `PATCH /api/admin/products/:id`
- `DELETE /api/admin/products/:id`

### Such- und Filteranforderungen

Die Produktliste soll serverseitig mindestens nach folgenden Kriterien filterbar sein:

- Name oder Suchbegriff
- Kategorie
- Aktiv-Status
- Verfuegbarkeit oder Lagerbestand

### Validierung

- Fehlende Pflichtfelder fuehren zu `400 Bad Request`.
- Ungueltige Kategorie-Referenzen fuehren zu `400 Bad Request` oder `404 Not Found`.
- Negative Preise oder Lagerbestaende werden abgelehnt.

### Loeschregeln

- Produkte koennen hart geloescht oder als inaktiv markiert werden.
- Fuer dieses Projekt wird bevorzugt: Produkt auf `aktiv = false` setzen, damit bestehende Bestellungen konsistent bleiben.
- Bereits bestellte Produkte duerfen historische Bestelldaten nicht unbrauchbar machen.

### Auswirkungen auf den Shop

- Aktive Produkte sind im oeffentlichen Shop sichtbar, sofern sie einer aktiven Kategorie zugeordnet sind.
- Inaktive Produkte duerfen im oeffentlichen Katalog nicht erscheinen.
- Preis- und Bestandsaenderungen wirken sich auf zukuenftige Warenkorb- und Bestellvorgaenge aus.

### Fehlerfaelle

- Produkt nicht gefunden: `404 Not Found`
- Konflikt durch ungueltigen Statuswechsel oder Datenstand: `409 Conflict`, falls benoetigt
- Unerwarteter Serverfehler: `500 Internal Server Error`

### Akzeptanzkriterien

- Ein Admin kann ein neues Produkt mit Kategoriezuordnung speichern.
- Ein Admin kann Preis, Beschreibung, Bild-Referenz und Lagerbestand anpassen.
- Ein Admin kann ein Produkt deaktivieren, sodass es im Shop nicht mehr sichtbar ist.

## Use Case UC-12: Kategorien verwalten

### Ziel

Admins koennen die Katalogstruktur pflegen.

### Erforderliche Backend-Funktionen

- Kategorieliste abrufen
- Kategoriedetails abrufen
- Kategorie anlegen
- Kategorie bearbeiten
- Kategorie aktivieren oder deaktivieren
- Kategorie loeschen

### Empfohlene Endpunkte

- `GET /api/admin/categories`
- `GET /api/admin/categories/:id`
- `POST /api/admin/categories`
- `PUT /api/admin/categories/:id`
- `PATCH /api/admin/categories/:id`
- `DELETE /api/admin/categories/:id`

### Validierung

- Leere Kategorienamen werden abgelehnt.
- Doppelte Kategorienamen werden abgelehnt.
- Zu loeschende Kategorien muessen hinsichtlich ihrer Produktzuordnungen geprueft werden.

### Loesch- und Aenderungsregeln

- Wird eine Kategorie deaktiviert, erscheint sie nicht mehr in der oeffentlichen Navigation.
- Produkte mit ausschliesslich inaktiven oder entfernten Kategorien duerfen im Shop nicht regulaer sichtbar sein.
- Beim Loeschen einer Kategorie muss das Backend eine der folgenden Regeln erzwingen:
- Loeschen nur erlauben, wenn keine Produkte mehr zugeordnet sind.
- Alternativ: Produkte muessen vor dem Loeschen aktiv umgehaengt werden.

Fuer dieses Projekt wird bevorzugt:

- Loeschen nur erlauben, wenn keine Produkte mehr zugeordnet sind.

### Fehlerfaelle

- Kategorie nicht gefunden: `404 Not Found`
- Kategorie noch Produkten zugeordnet: `409 Conflict`
- Unerwarteter Serverfehler: `500 Internal Server Error`

### Akzeptanzkriterien

- Ein Admin kann eine neue Kategorie anlegen.
- Ein Admin kann Name und Beschreibung einer Kategorie aktualisieren.
- Eine Kategorie kann nicht geloescht werden, solange Produkte zugeordnet sind.

## API-Antworten

### Erfolgsantworten

- Listen liefern strukturierte Arrays mit Pagination optional.
- Detailansichten liefern ein einzelnes Objekt.
- Schreiboperationen liefern den aktualisierten Datensatz oder eine bestaetigende Erfolgsnachricht.

### Fehlerantworten

Fehlerantworten sollen konsistent sein und mindestens enthalten:

- `code`
- `message`
- optional `fieldErrors`

## Technische Hinweise

- Admin-Endpunkte sollen sauber von Shop-Endpunkten getrennt sein.
- Rollenpruefung soll zentral in Middleware oder Guard-Logik erfolgen.
- Schreiboperationen auf Produkte und Kategorien sollen nachvollziehbar loggbar sein, falls spaeter Audit-Trails eingefuehrt werden.

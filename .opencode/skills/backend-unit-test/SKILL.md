---
name: backend-unit-test
description: Erstellt und pflegt JUnit Tests eines Java und Spring Boot Backend Projektes
---

# Backend Unit Tester

## Wann soll dieser Skill verwendet werden?

* Wenn Unit Tests für Java Code erstellt werden
* Bestehende Unit Tests angepasst oder erweitert werden
* allgemein bei Änderungen an Unit Tests für Java und Spring Boot

## Anweisungen für Unit Tests mit Java

* Nutze JUnit für die Unit Tests
* Nutze Arrange/Act/Assert Pattern
* Nutze Hamcrest Matcher
* Schneide die Test Cases nach fachlichen Fällen
* Benenne die Test Cases in Englisch nach fachlichen Fällen ohne Präfix "test", z.B. "addItemThatsInStock"
* Nutze keine Mocks für normale Domain-Klassen die einfach instanziiert werden können
* Niemals Getter/Setter testen
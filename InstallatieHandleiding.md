# 🌿 Plantenkennis API

**Eindopdracht Backend 2.0 -- NOVI ICT**\
**Student:** Koen Moret (9001019401)

------------------------------------------------------------------------

# 📑 Inhoudsopgave

1.  Inleiding\
2.  Functionaliteiten\
3.  Projectstructuur\
4.  Gebruikte technieken\
5.  Installatiehandleiding\
6.  Configuratie\
7.  Security & Rollen\
8.  Testen uitvoeren\
9.  API-documentatie\
10. Postman collectie

------------------------------------------------------------------------

# 1️⃣ Inleiding

De **Plantenkennis API** is een Spring Boot REST web-API voor het
beheren van:

-   Plantsoorten
-   Kenmerken
-   Foto's (met filesystem opslag)
-   Gebruikers
-   Favorieten
-   Spelsessies

De API ondersteunt **JWT-authenticatie** en **Role Based Access Control
(RBAC)**.

Deze applicatie is ontwikkeld als eindopdracht voor Backend 2.0 (NOVI
ICT).

------------------------------------------------------------------------

# 2️⃣ Functionaliteiten

-   CRUD-operaties voor plantsoorten\
-   Many-to-Many relatie via koppelentiteit (plant ↔ kenmerken)\
-   Foto-upload met opslag op filesystem\
-   Favorieten per gebruiker\
-   Spelsessies registreren\
-   JWT-beveiliging via OAuth2 Resource Server\
-   Rollen: Admin / Deelnemer\
-   Unit tests + Integratietests + Security tests

------------------------------------------------------------------------

# 3️⃣ Projectstructuur

    src/main/java/nl/novi/plantenkennis
     ├── controller
     ├── service
     ├── repository
     ├── entity
     ├── dto
     ├── mapper
     ├── config
     └── exception

**Laagverdeling:**

-   Controller → REST endpoints\
-   Service → Business logica\
-   Repository → Database toegang (Spring Data JPA)\
-   Entity → Database modellen\
-   DTO → API responses\
-   Mapper → Conversie entity ↔ DTO\
-   Config → Security configuratie\
-   Exception → Custom exception handling

------------------------------------------------------------------------

# 4️⃣ Gebruikte technieken

-   Java 21 (LTS)\
-   Spring Boot 3.5.9\
-   Spring Security\
-   OAuth2 Resource Server\
-   JWT\
-   Maven\
-   PostgreSQL (productie)\
-   H2 (tests)\
-   JUnit 5\
-   Mockito\
-   MockMvc

------------------------------------------------------------------------

# 5️⃣ Installatiehandleiding

## Vereisten

-   Java 21
-   Maven
-   PostgreSQL
-   Keycloak

------------------------------------------------------------------------

## Stap 1 -- Repository clonen

    git clone https://github.com/koenmoret/plantenkennis-api.git
    cd plantenkennis-api

------------------------------------------------------------------------

## Stap 2 -- Database aanmaken

    CREATE DATABASE plantenkennis;

------------------------------------------------------------------------

## Stap 3 -- application.properties configureren

Configureer:

    spring.datasource.url=jdbc:postgresql://localhost:5432/plantenkennis
    spring.datasource.username=postgres
    spring.datasource.password=********

    issuer-uri=http://localhost:8080/realms/plantenkennis
    client-id=plantenkennis-client
    audience=plantenkennis-api

------------------------------------------------------------------------

## Stap 4 -- Applicatie starten

    mvn clean install
    mvn spring-boot:run

De API draait op:

    http://localhost:8080

------------------------------------------------------------------------

# 6️⃣ Security & Rollen

### Client configuratie (Keycloak)
client_id: plantenkennis-api-client\
client_secret: fNfnzB21YiPXrum5VH7pydNfY8ubZyRQ

## Rollen:

-   ROLE_client_admin
-   ROLE_client_deelnemer

## Testgebruikers

| Gebruiker   | Rol                   | Wachtwoord  |
|:------------|:----------------------|------------:|
| beheerder1  | ROLE_client_admin     | Welkom123!  |
| deelnemer1  | ROLE_client_deelnemer | Welkom123!  |


------------------------------------------------------------------------

# 7️⃣ Testen uitvoeren

    mvn test

Tests gebruiken:

-   H2 in-memory database
-   application-test.properties
-   ddl-auto=create-drop

------------------------------------------------------------------------

# 8️⃣ API-documentatie

## Publiek toegankelijke endpoints

| Methode | Endpoint                        | Toegang |
|----------|---------------------------------|----------|
| GET      | /plantsoorten/**                | Publiek  |
| GET      | /kenmerken/**                   | Publiek  |
| GET      | /plantsoorten/{id}/fotos        | Publiek  |


------------------------------------------------------------------------

## Admin endpoints

| Methode | Endpoint                                   | Toegang |
|----------|--------------------------------------------|----------|
| POST     | /plantsoorten/**                           | Admin    |
| DELETE   | /plantsoorten/**                           | Admin    |
| POST     | /plantsoorten/{id}/fotos                   | Admin    |
| DELETE   | /plantsoorten/{plantId}/fotos/{fotoId}     | Admin    |
| POST     | /kenmerken/**                              | Admin    |
| DELETE   | /kenmerken/**                              | Admin    |


------------------------------------------------------------------------

### Deelnemer endpoints

| Methode | Endpoint            | Toegang     |
|----------|--------------------|-------------|
| GET      | /spelsessies/**    | Deelnemer  |
| GET      | /gebruikers/**     | Deelnemer  |
| GET      | /auth/admin        | Admin      |


------------------------------------------------------------------------

# 9️⃣ Postman collectie

De Postman collectie is beschikbaar als aparte `.json` export en bevat:

-   JWT-auth flows
-   Admin requests
-   Deelnemer requests
-   Publieke requests

------------------------------------------------------------------------


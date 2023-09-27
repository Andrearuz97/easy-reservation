# Easy Reservation - Backend

Applicazione Spring Boot per la prenotazione di stanze di hotel.

## Tecnologie e Dipendenze

- Spring Boot (v3.1.3): Framework principale per l'applicazione backend.
- Java (v17): Linguaggio di programmazione utilizzato.
- Spring Data JPA: Per l'interazione con il database.
- Spring Security: Per gestire l'autenticazione e la sicurezza.
- PostgreSQL: Database utilizzato.
- Lombok: Utilizzato per ridurre il codice boilerplate.
- JWT (JSON Web Tokens) (v0.11.5): Per l'autenticazione stateless.
- Java Validation API (v2.0.1.Final): Per la validazione delle email.
- Spring Boot Devtools: Per funzionalità di sviluppo come il "hot reload".

## Prerequisiti

- Java 17
- Maven
- PostgreSQL

## Configurazione

- Assicurati di avere una istanza di PostgreSQL in esecuzione. Modifica il file `src/main/resources/application.properties` per configurare l'URL, l'username e la password del database.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_del_tuo_db
spring.datasource.username=tuo_username
spring.datasource.password=tua_password
```
#Avvio
 - Naviga nella directory principale del progetto (dove si trova il pom.xml).
 - Avvia l'applicazione con il comando:mvn spring-boot:run
 - L'applicazione si avvierà sulla porta 8080 (o sulla porta specificata nel tuo ('application.properties').
#Frontend
 - Il frontend di questa applicazione è sviluppato con Angular. Puoi trovare il codice sorgente e le istruzioni per avviarlo nel seguente repository:
 - `https://github.com/Andrearuz97/EasyReservation-Angular`


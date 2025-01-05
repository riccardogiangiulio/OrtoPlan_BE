# OrtoPlan - Backend

## Descrizione
OrtoPlan è un'applicazione per la gestione di piantagioni orticole che permette agli utenti di pianificare e monitorare le proprie coltivazioni. Il sistema tiene traccia delle attività necessarie per ogni piantagione e invia notifiche per le attività programmate.

## Requisiti
- JDK 21
- Maven 3.6+
- PostgreSQL 15+

## Setup Rapido
1. Creare database PostgreSQL: `CREATE DATABASE orto_plan;`
2. Eseguire gli script in `scripts/`:
   - `ddl.sql`: Creazione tabelle
   - `dml.sql`: Dati iniziali
3. Configurare `DatabaseConnection.java` con le credenziali del database
4. Eseguire `mvn clean install`
5. Avviare con `mvn exec:java -Dexec.mainClass="com.riccardo.giangiulio.App"`

## Endpoint API

### Autenticazione
```http
POST /login
Body: { "email": "admin@ortoplan.com", "password": "admin123" }
Response: { "token": "jwt_token" }
```

### User
```http
POST   /users              # Registrazione nuovo utente
GET    /users/{userId}     # Recupera dati utente
PUT    /users/{userId}     # Aggiorna dati utente
DELETE /users/{userId}     # Elimina utente
```

### Plant
```http
POST   /plants                # Crea nuova pianta
GET    /plants               # Lista tutte le piante
GET    /plants/{plantId}     # Dettagli pianta
PUT    /plants/{plantId}     # Aggiorna pianta
DELETE /plants/{plantId}     # Elimina pianta
```

### Plantation
```http
POST   /plantations                    # Crea piantagione
GET    /plantations/user/{userId}      # Piantagioni utente
GET    /plantations/{plantationId}     # Dettagli piantagione
PUT    /plantations/{plantationId}     # Aggiorna piantagione
DELETE /plantations/{plantationId}     # Elimina piantagione
```

### Activity
```http
POST   /activities                                # Crea attività
GET    /activities/plantation/{plantationId}      # Attività piantagione
GET    /activities/plantation/{plantationId}/pending  # Attività pendenti
PUT    /activities/{activityId}                   # Aggiorna attività
DELETE /activities/{activityId}                   # Elimina attività
```

### ActivityType
```http
POST   /activityTypes                    # Crea tipo attività
GET    /activityTypes                    # Lista tipi attività
GET    /activityTypes/{activityTypeId}   # Dettagli tipo attività
DELETE /activityTypes/{activityTypeId}   # Elimina tipo attività
```

### Notification
```http
POST   /notifications                    # Crea notifica
GET    /notifications/unread/{userId}    # Notifiche non lette
PUT    /notifications/{notificationId}/read  # Segna come letta
DELETE /notifications/{notificationId}    # Elimina notifica
```

## Note
- Tutte le richieste autenticate richiedono header: `Authorization: Bearer <token>`
- Date formato: "yyyy-MM-dd"
- Timestamp formato: "yyyy-MM-dd HH:mm:ss"
- Risposte in formato JSON

## Credenziali Default
- Email: admin@ortoplan.com
- Password: admin123 
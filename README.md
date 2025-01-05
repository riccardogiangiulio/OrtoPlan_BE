# OrtoPlan - Backend

OrtoPlan è un'applicazione per la gestione di piantagioni orticole. Questo repository contiene il backend dell'applicazione.

## Requisiti di Sistema

- Java Development Kit (JDK) 21 o superiore
- Maven 3.6 o superiore
- PostgreSQL 15 o superiore
- Git

## Setup del Database

1. Installare PostgreSQL sul proprio sistema
2. Creare un nuovo database chiamato `orto_plan`:
```sql
CREATE DATABASE orto_plan;
```

3. Eseguire i seguenti script DDL per creare le tabelle necessarie:

```sql
-- Creazione delle tabelle
CREATE TABLE public."User" (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE public."Plant" (
    plant_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    cultivation_start DATE NOT NULL,
    cultivation_end DATE NOT NULL,
    harvest_time INTEGER NOT NULL
);

CREATE TABLE public."Plantation" (
    plantation_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    user_id INTEGER REFERENCES public."User"(user_id) ON DELETE CASCADE,
    plant_id INTEGER REFERENCES public."Plant"(plant_id) ON DELETE CASCADE
);

CREATE TABLE public."ActivityType" (
    type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE public."Activity" (
    activity_id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    scheduled_dt TIMESTAMP NOT NULL,
    completed BOOLEAN DEFAULT false,
    type_id INTEGER REFERENCES public."ActivityType"(type_id) ON DELETE CASCADE,
    plantation_id INTEGER REFERENCES public."Plantation"(plantation_id) ON DELETE CASCADE
);

CREATE TABLE public."Notification" (
    notification_id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    sent_dt TIMESTAMP NOT NULL,
    opened BOOLEAN DEFAULT false,
    activity_id INTEGER REFERENCES public."Activity"(activity_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES public."User"(user_id) ON DELETE CASCADE
);
```

4. Inserire alcuni dati di esempio (opzionale):

```sql
-- Inserimento utente di default per il login
INSERT INTO public."User" (first_name, last_name, email, password)
VALUES ('Admin', 'User', 'admin@ortoplan.com', '$2a$10$xLxAYXMGZYNJF1oYsHgK8.0qL7FFnqB3zw5G0hQxhxF4S0SqO4Tmi');
-- La password è 'admin123'

-- Inserimento tipi di attività di base
INSERT INTO public."ActivityType" (name) VALUES 
('Semina'),
('Irrigazione'),
('Concimazione'),
('Raccolta'),
('Potatura');

-- Inserimento piante di esempio
INSERT INTO public."Plant" (name, description, cultivation_start, cultivation_end, harvest_time)
VALUES 
('Pomodoro', 'Varietà San Marzano', '2024-03-01', '2024-08-31', 90),
('Lattuga', 'Varietà Iceberg', '2024-03-01', '2024-05-31', 45),
('Carota', 'Varietà Nantese', '2024-03-01', '2024-06-30', 75);
```

## Configurazione del Progetto

1. Clonare il repository:
```bash
git clone https://github.com/tuoUsername/OrtoPlan_BE.git
cd OrtoPlan_BE
```

2. Modificare le credenziali del database nel file `src/main/java/com/riccardo/giangiulio/utility/database/DatabaseConnection.java`:
```java
private final String UrlDB = "jdbc:postgresql://localhost:5434/orto_plan";
private final String userDB = "postgres";
private final String pwdDB = "tuaPassword";
```

3. Installare le dipendenze con Maven:
```bash
mvn clean install
```

## Avvio dell'Applicazione

1. Avviare l'applicazione:
```bash
mvn exec:java -Dexec.mainClass="com.riccardo.giangiulio.App"
```

L'applicazione sarà disponibile all'indirizzo `http://localhost:7070`

## Credenziali di Default

Per il primo accesso, utilizzare le seguenti credenziali:
- Email: admin@ortoplan.com
- Password: admin123

## Note Aggiuntive

- L'applicazione utilizza JWT per l'autenticazione. I token hanno una validità di 1 ora.
- Tutte le password vengono hashate utilizzando BCrypt prima di essere salvate nel database.
- Le date devono essere fornite nel formato "yyyy-MM-dd".
- Le date/ore devono essere fornite nel formato "yyyy-MM-dd HH:mm:ss".
- Per le richieste autenticate, includere il token JWT nell'header Authorization: `Bearer <token>`

## Troubleshooting

1. Errore di connessione al database:
   - Verificare che PostgreSQL sia in esecuzione
   - Controllare le credenziali nel file `DatabaseConnection.java`
   - Verificare che il database `orto_plan` sia stato creato

2. Errore "Port already in use":
   - Verificare che la porta 7070 non sia già in uso
   - Modificare la porta nel file `App.java` se necessario

3. Errore di compilazione:
   - Verificare di avere JDK 21 installato
   - Eseguire `mvn clean install` per rigenerare i file compilati 
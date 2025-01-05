DROP TABLE IF EXISTS public."Notification" CASCADE; -- Dipende da Activity e User
DROP TABLE IF EXISTS public."Activity" CASCADE;     -- Dipende da Plantation e ActivityType
DROP TABLE IF EXISTS public."ActivityType" CASCADE;
DROP TABLE IF EXISTS public."Plantation" CASCADE;   -- Dipende da User e Plant
DROP TABLE IF EXISTS public."Plant" CASCADE;
DROP TABLE IF EXISTS public."User" CASCADE;

CREATE TABLE public."User" (
    user_id BIGSERIAL PRIMARY KEY,           -- Unique identifier for each user
    first_name VARCHAR(100) NOT NULL,       -- User's first name
    last_name VARCHAR(100) NOT NULL,        -- User's last name
    email VARCHAR(100) UNIQUE NOT NULL,     -- Unique email for authentication
    password VARCHAR(100) NOT NULL          -- Encrypted password
);

CREATE TABLE public."Plant" (
    plant_id BIGSERIAL PRIMARY KEY,          -- Unique identifier for the plant
    name VARCHAR(100) NOT NULL,             -- Name of the plant
    description TEXT,                       -- Description of the plant
    cultivation_start DATE NOT NULL,        -- Start of the ideal cultivation period
    cultivation_end DATE NOT NULL,          -- End of the ideal cultivation period
    harvest_time INT NOT NULL               -- Time in days required for harvesting
);

CREATE TABLE public."Plantation" (
    plantation_id BIGSERIAL PRIMARY KEY,     -- Unique identifier for the plantation
    name VARCHAR(100) NOT NULL,             -- Name of the plantation
    start_date DATE NOT NULL,               -- Plantation start date
    end_date DATE NOT NULL,                 -- Plantation end date
    city VARCHAR(100) NOT NULL,             -- City of the plantation (for weather data)
    user_id BIGINT NOT NULL,                -- User who owns the plantation
    plant_id BIGINT NOT NULL,               -- Type of plant cultivated
    FOREIGN KEY (user_id) REFERENCES public."User"(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,   -- Relationship with the User table
    FOREIGN KEY (plant_id) REFERENCES public."Plant"(plant_id)
        ON DELETE CASCADE ON UPDATE CASCADE    -- Relationship with the Plant table
);

CREATE TABLE public."ActivityType" (
    type_id BIGSERIAL PRIMARY KEY,          -- Unique identifier for the activity type
    name VARCHAR(50) NOT NULL UNIQUE        -- Name of the activity type
);

CREATE TABLE public."Activity" (
    activity_id BIGSERIAL PRIMARY KEY,      -- Unique identifier for the activity
    description TEXT NOT NULL,             -- Description of the activity
    scheduled_dt TIMESTAMP NOT NULL,       -- Scheduled date and time
    completed BOOLEAN DEFAULT FALSE,       -- Activity status
    plantation_id BIGINT NOT NULL,         -- Reference to the plantation
    type_id BIGINT NOT NULL,               -- Reference to the activity type
    FOREIGN KEY (plantation_id) REFERENCES public."Plantation"(plantation_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (type_id) REFERENCES public."ActivityType"(type_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE public."Notification" (
    notification_id BIGSERIAL PRIMARY KEY,  -- Unique identifier for the notification
    message TEXT NOT NULL,                 -- Notification message
    opened BOOLEAN DEFAULT FALSE,          -- Notification read status
    sent_dt TIMESTAMP NOT NULL,            -- Notification sent date and time
    user_id BIGINT NOT NULL,               -- User receiving the notification
    activity_id BIGINT NOT NULL,           -- Reference to the related activity
    FOREIGN KEY (user_id) REFERENCES public."User"(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,   -- Relationship with the User table
    FOREIGN KEY (activity_id) REFERENCES public."Activity"(activity_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

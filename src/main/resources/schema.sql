CREATE TABLE IF NOT EXISTS owners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    phone VARCHAR(64),
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS vets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    species VARCHAR(255) NOT NULL,
    breed VARCHAR(255),
    owner_id BIGINT NOT NULL REFERENCES owners (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets (id) ON DELETE CASCADE,
    vet_id BIGINT NOT NULL REFERENCES vets (id) ON DELETE RESTRICT,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(1024),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_appointments_vet_slot UNIQUE (vet_id, date_time)
);

CREATE TABLE IF NOT EXISTS treatments (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE REFERENCES appointments (id) ON DELETE CASCADE,
    procedures_and_medications VARCHAR(2000),
    notes VARCHAR(2000)
);

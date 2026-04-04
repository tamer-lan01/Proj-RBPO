TRUNCATE TABLE treatments, appointments, pets, vets, owners RESTART IDENTITY CASCADE;

INSERT INTO owners (name, last_name, phone, email)
VALUES ('Ivan', 'Ivanov', '123456', 'ivan@example.com'),
       ('Maria', 'Petrova', '654321', 'maria@example.com');

INSERT INTO vets (name, specialization)
VALUES ('Dr. Smith', 'Therapy'),
       ('Dr. Jones', 'Surgery');

INSERT INTO pets (name, species, breed, owner_id)
VALUES ('Doggie', 'Dog', 'Labrador', 1),
       ('Kitty', 'Cat', 'Siamese', 2);

INSERT INTO appointments (pet_id, vet_id, visit_date, reason, completed)
VALUES (1, 1, '2000-01-01', 'Checkup', TRUE),
       (2, 2, '2000-01-02', 'Follow-up', FALSE);

INSERT INTO treatments (appointment_id, procedures_and_medications, notes)
VALUES (1, 'Vitamin injection', 'First visit treatment');

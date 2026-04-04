-- Однократная миграция: старая колонка date_time → visit_date (DATE).
-- Выполни в pgAdmin (Query Tool) для базы proj под пользователем с правами на ALTER.
-- Если колонка уже называется visit_date — скрипт не запускай (будет ошибка на RENAME).

ALTER TABLE appointments DROP CONSTRAINT IF EXISTS uq_appointments_vet_slot;
ALTER TABLE appointments DROP CONSTRAINT IF EXISTS uq_appointments_vet_day;

ALTER TABLE appointments RENAME COLUMN date_time TO visit_date;
ALTER TABLE appointments ALTER COLUMN visit_date TYPE date USING (visit_date::date);

ALTER TABLE appointments ADD CONSTRAINT uq_appointments_vet_day UNIQUE (vet_id, visit_date);

-- Если в app_users остались старые значения STAFF/VET после смены модели ролей на ADMIN/USER:
UPDATE app_users SET role = 'USER' WHERE role IN ('STAFF', 'VET');

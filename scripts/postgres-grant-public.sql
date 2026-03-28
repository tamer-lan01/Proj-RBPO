-- Шаблон для pgAdmin (Query Tool), подключение под postgres к базе proj.
-- Подставьте имя своей роли из Login/Group Roles (у вас, например: user_proj).

-- Самый простой вариант — владелец базы:
ALTER DATABASE proj OWNER TO user_proj;

-- Если не хотите менять владельца, достаточно прав на схему public:
-- GRANT USAGE, CREATE ON SCHEMA public TO user_proj;

-- Если таблицы уже созданы под другим пользователем:
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO user_proj;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO user_proj;

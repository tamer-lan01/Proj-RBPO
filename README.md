# Ветклиника (РБПО, задание 3)

## Тема

Сервис для предметной области **ветеринарная клиника**: учёт владельцев животных, питомцев, врачей, визитов (приёмов) и назначений (лечение/процедуры).

## Сущности

| Сущность       | Назначение |
|----------------|------------|
| **Owner**      | Владелец питомца |
| **Pet**        | Питомец, привязан к владельцу |
| **Vet**        | Врач |
| **Appointment**| Визит: питомец + врач + дата/время + причина; флаг `completed` |
| **Treatment**  | Назначение по визиту (не более одного на визит) |

Связи в БД: `pets.owner_id → owners`, `appointments` → `pets` и `vets`, `treatments.appointment_id → appointments`.

Ограничения: уникальный `owners.email`, уникальная пара `(vet_id, date_time)` для слотов врача, уникальный `treatments.appointment_id`.

## Запуск

1. PostgreSQL: создайте БД (в `application.properties` по умолчанию `jdbc:postgresql://localhost:5432/proj`).
2. Переменные окружения (секреты не хранятся в репозитории):
   - `DB_USER` — пользователь PostgreSQL  
   - `DB_PASS` — пароль  
3. Сборка и запуск: `mvn spring-boot:run` или через IDE (главный класс `RBPO.proj.ProjApplication`).

При старте выполняются `schema.sql` (таблицы) и `data.sql` (тестовые данные; таблицы предварительно очищаются).

### Ошибка «нет доступа к схеме public»

Типично для **PostgreSQL 15+**: роль из `DB_USER` не может создавать объекты в схеме `public`, поэтому падает первый `CREATE TABLE` из `schema.sql`.

**Что сделать:** в pgAdmin под пользователем **postgres** (или владельцем БД) откройте базу `proj`, Query Tool, выполните (подставьте своего пользователя вместо `app_user`):

```sql
GRANT USAGE, CREATE ON SCHEMA public TO имя_вашей_роли;
```

Имя роли смотрите в pgAdmin: **Login/Group Roles** (например `user_proj`). Это же имя должно быть в **`DB_USER`** при запуске приложения. В шаблоне `scripts/postgres-grant-public.sql` подставьте роль вместо плейсхолдера `YOUR_ROLE`.

**Альтернатива:** сделать эту роль **владельцем** базы `proj` (часто проще):

```sql
ALTER DATABASE proj OWNER TO имя_вашей_роли;
```

## API

- **CRUD** (задание 2):  
  `/api/owners`, `/api/pets`, `/api/vets`, `/api/appointments`, `/api/treatments`  
  Данные читаются и пишутся в PostgreSQL через Spring Data JPA.

- **Бизнес-операции** (задание 3, не менее 2 сущностей в одной операции / в запросе):
  1. `POST /api/operations/admit-pet-with-appointment` — завести питомца и сразу записать на приём (транзакция: Pet + Appointment).
  2. `POST /api/operations/complete-visit-with-treatment` — после визита оформить назначение и пометить визит завершённым (Treatment + Appointment).
  3. `GET /api/operations/owners/{ownerId}/summary` — владелец и список его питомцев (Owner + Pet).
  4. `GET /api/operations/vets/{vetId}/schedule?from=...&to=...` — расписание врача с именами питомцев (Vet + Appointment + Pet). Время в формате ISO-8601.
  5. `POST /api/operations/cancel-appointment` — тело `{"appointmentId": ...}`: отмена визита (связанное назначение удаляется каскадом в БД).

## Диаграмма связей (ERD) в pgAdmin 4

Чтобы показать на защите **взаимосвязи таблиц** (как в примере с картинкой):

1. Подключитесь к серверу PostgreSQL и откройте базу (например `proj`).
2. В меню **Tools** выберите **ERD Tool** (в некоторых версиях: **Schema Diff** рядом нет — ищите именно **ERD Tool**).
3. В открывшемся окне ERD добавьте схему **public** и таблицы `owners`, `pets`, `vets`, `appointments`, `treatments` (или выберите «все таблицы схемы»).
4. Сохраните или экспортируйте изображение (**Save** / **Download image** — зависит от версии pgAdmin).

Если пункта ERD нет: обновите pgAdmin до актуальной версии или покажите связи через **Properties таблицы → Constraints → Foreign key** по каждой таблице.

## Postman

1. **Import** → файл `VetClinic.postman_collection.json`.
2. В коллекции переменная **baseUrl** = `http://localhost:8080` (если порт другой — измените).
3. Порядок для демонстрации: папка **Lists** (все GET) → CRUD по папкам → **Operations (assignment 3)**. Подробности и предупреждения — в описании коллекции в Postman (вкладка коллекции → описание).

## Тесты

Профиль `test` использует in-memory H2 (`src/test/resources/application-test.properties`), чтобы `mvn test` не требовал PostgreSQL.

# Ветклиника (РБПО, задание 3)

## Тема

Сервис для предметной области **ветеринарная клиника**: учёт владельцев животных, питомцев, врачей, визитов (приёмов) и назначений (лечение/процедуры).

## Сущности

| Сущность       | Назначение |
|----------------|------------|
| **Owner**      | Владелец питомца |
| **Pet**        | Питомец, привязан к владельцу |
| **Vet**        | Врач |
| **Appointment**| Визит: питомец + врач + **день приёма** (`visit_date`, без времени) + причина; флаг `completed` |
| **Treatment**  | Назначение по визиту (не более одного на визит) |

Связи в БД: `pets.owner_id → owners`, `appointments` → `pets` и `vets`, `treatments.appointment_id → appointments`.

Ограничения: уникальный `owners.email`, у врача не больше одного приёма в календарный день `(vet_id, visit_date)`, уникальный `treatments.appointment_id`.

## Запуск

1. PostgreSQL: создайте БД (в `application.properties` по умолчанию `jdbc:postgresql://localhost:5432/proj`).
2. Переменные окружения (секреты не хранятся в репозитории):
   - `DB_USER` — пользователь PostgreSQL  
   - `DB_PASS` — пароль  
3. Сборка и запуск: `mvn spring-boot:run` или через IDE (главный класс `RBPO.proj.ProjApplication`).

При старте выполняются `schema.sql` (таблицы) и `data.sql` (тестовые данные; таблицы предварительно очищаются).

### Ошибка «столбец visit_date не существует» (PostgreSQL)

Таблица `appointments` уже была создана **со старой** колонкой `date_time`. Команда `CREATE TABLE IF NOT EXISTS` **не меняет** существующую таблицу, поэтому `data.sql` падает.

**Вариант A (сохранить данные в других таблицах):** в pgAdmin открой базу `proj` → **Query Tool** → выполни скрипт `scripts/postgres-migrate-appointments-visit-date.sql` (или те же команды из него). Затем снова запусти приложение.

**Вариант B (чистая лаба):** удали базу `proj`, создай заново, запусти приложение — выполнится актуальный `schema.sql` с `visit_date`.

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
  **Appointment** в JSON: поле **`visitDate`** — одна дата **`yyyy-MM-dd`** (время не используется).

- **Бизнес-операции** (задание 3, не менее 2 сущностей в одной операции / в запросе):
  1. `POST /api/operations/admit-pet-with-appointment` — завести питомца и сразу записать на приём (транзакция: Pet + Appointment).
  2. `POST /api/operations/complete-visit-with-treatment` — после визита оформить назначение и пометить визит завершённым (Treatment + Appointment).
  3. `GET /api/operations/owners/{ownerId}/summary` — владелец и список его питомцев (Owner + Pet).
  4. `GET /api/operations/vets/{vetId}/schedule?from=...&to=...` — расписание врача с именами питомцев (Vet + Appointment + Pet). Параметры **from** и **to** — даты `yyyy-MM-dd` (диапазон включительно по календарным дням).
  5. `POST /api/operations/cancel-appointment` — тело `{"appointmentId": ...}`: отмена визита (связанное назначение удаляется каскадом в БД).

## Задание 4. Базовая безопасность API

Соответствует [заданию 4 в README РБПО](https://github.com/MatorinFedor/RBPO_2025_demo/blob/master/README.md): **Spring Security**, **HTTP Basic**, **CSRF** (cookie `XSRF-TOKEN` + заголовок `X-XSRF-TOKEN` на изменяющие запросы). Регистрация **без пользователей в `data.sql` и без паролей в коде**; пароли в БД — **BCrypt**.

- **Два способа регистрации**  
  - **`POST /api/auth/register/user`** — роль **USER**.  
  - **`POST /api/auth/register/admin`** — роль **ADMIN**; обязателен заголовок **`X-Admin-Register-Secret`**, значение в **`application.properties`** (`app.security.admin-register-secret`) или **`APP_ADMIN_REGISTER_SECRET`** (в проде смените).
- **Права (как проверяет сервер)**  
  - **ADMIN** — любые эндпоинты под этим логином/паролем.  
  - **USER** — только **`POST /api/pets`**; остальное — **403**.  
  Роль на сервере **не переключается** отдельной командой: вы меняете **учётную запись** в HTTP Basic (другой email/пароль — другая роль).
- **Публично (без Basic)**: `GET /api/hello`, оба `POST /api/auth/register/...`, `GET /api/auth/csrf` (отдельная цепочка Security без Basic, см. `SecurityConfig`).
- Таблица учёток: `scripts/postgres-app-users.sql`; миграция старых ролей: `scripts/postgres-app-users-migrate-roles-to-admin-user.sql`.
- **Postman**: папка **«Лаба 4 — проверка ролей»** — запросы с явным Basic админа или юзера; запросы **«Переключить коллекцию на ADMIN/USER»** обновляют переменные `basicUser`/`basicPassword` для остальных запросов коллекции (в т.ч. в **Pre-request** до отправки).

## Диаграмма связей (ERD) в pgAdmin 4

Чтобы показать на защите **взаимосвязи таблиц** (как в примере с картинкой):

1. Подключитесь к серверу PostgreSQL и откройте базу (например `proj`).
2. В меню **Tools** выберите **ERD Tool** (в некоторых версиях: **Schema Diff** рядом нет — ищите именно **ERD Tool**).
3. В открывшемся окне ERD добавьте схему **public** и таблицы `app_users`, `owners`, `pets`, `vets`, `appointments`, `treatments` (или выберите «все таблицы схемы»).
4. Сохраните или экспортируйте изображение (**Save** / **Download image** — зависит от версии pgAdmin).

Если пункта ERD нет: обновите pgAdmin до актуальной версии или покажите связи через **Properties таблицы → Constraints → Foreign key** по каждой таблице.

## Postman

1. **Import** → файл `VetClinic.postman_collection.json`.
2. В коллекции во всех запросах указан `http://localhost:8080` (если порт другой — измените URL в запросах).
3. **После лабы 4:** **Auth** — Register ADMIN/USER, **Get CSRF**; **«Лаба 4 — проверка ролей»** — наглядно ADMIN vs USER; **«Переключить коллекцию…»** — подставить `basicUser` для папок Lists/CRUD. Переменные `adminEmail`, `adminPassword`, `userEmail`, `userPassword`, `csrfToken`.
4. Порядок для демонстрации данных: **Lists** → CRUD → **Operations**. Подробности — в описании коллекции в Postman.

## Тесты

Профиль `test` использует in-memory H2 (`src/test/resources/application-test.properties`), чтобы `mvn test` не требовал PostgreSQL.

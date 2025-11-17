LMS Spring Boot + JPA


Реализованы пользователи (STUDENT / TEACHER / ADMIN), запись на курсы, домашки, квизы и JWT-аутентификация.
Запуск - docker compose up --build

REST API
POST /api/auth/register - регистрация пользователя
POST /api/auth/login - логин по email + password, выдаёт новый токен.
POST /api/users - создать пользователя (без пароля, для простого CRUD по ТЗ)
POST /api/courses — создать курс (Только TEACHER/ADMIN)
POST /api/enrollments — записать студента на курс (роль STUDENT)

В репозитории настроен GA workflow .github/workflows/ci.yml

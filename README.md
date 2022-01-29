# Students Randomizer
### Веб-приложение для проведения опросов между случайными студентами из группы
Схема опроса, реализованного в приложении:
* Выбирается пара случайных студентов, не состоящих в одной команде.
* Первый студент задает вопрос второму.
* Случайным образом выбирается третий студент, не состоящий в одной команде со вторым.
* Второй студент задает вопрос третьему.
* Выбирается четвертый студент, не состоящий в одной команде с третьим и т.д.
* Каждый студент задает один вопрос и отвечает на один вопрос.
### Основной функционал
* По адресу localhost:8080/students доступна таблица со всеми студентами группы.
* На главной странице можно добавлять новых студентов в таблицу.
* По имени студента можно перейти на его страницу, где есть возможность удалить его или отредактировать данные.
* По ссылке на главной странице можно начать опрос студентов в случайном порядке.
* На странице генерации студентов можно добавлять баллы за вопросы и ответы, а также можно начать генерацию заново.
* Пары спрашивающих и отвечающих логгируются в консоль.
### Технологии в проекте
* Java 11
* Spring Boot 2.6.3
* MySQL 8.0.26
* Thymeleaf
* Liquibase
* Hibernate 5.6.3.Final
### Как запустить?
* Склонировать репозиторий.
* В файле application.properties изменить имя пользователя и пароль для доступа к БД.
* Запустить метод main() в классе StudentsRandomizerApplication.
* Перейти в браузере по адресу localhost:8080/students.
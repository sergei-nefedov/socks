<h3>Демонстрационное RESTful приложение.</h3>

Язык реализации: Java 17<br>
Фреймворк: Spring Boot 2.7<br>
База данных:  PostgreSQL<br>
Версия JDK : 17<br>
Система сборки: Gradle<br>
Формат файла для загрузки партий: Excel (.xlsx)<br>
Тестирование: Rest Assured, JUnit, Testcontainers, SLF4J<br>
Прочее: Mapstruct, Lombok, SLF4J<br>
 <br> Запуск осуществляется через pers/nefedov/socks/SocksApplication.java. <br>
Для работы приложения необходимо запустить PostgreSQL в контейнере Docker с помощью compose.yaml

Описание эндпойнтов с примерами запросов реализовано с помощью Swagger, интерфейс которого находится по адресу http://localhost:8080/swagger-ui/index.html. <br><br>
Файл test.xlsx, используемый для тестирования загрузки партий, располагается в src/test/resources

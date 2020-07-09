# Order System Demo
This is a Gradle project by using Java, Spring Boot 2, JOOQ, and Flyway 
to demonstrate a simple and production ready RESTful order backend system 
powered by Google Map API service.

# Tech Stack
- Java
- Spring Boot 2
- JOOQ - DB query and code generation
- MySQL
- Flyway - DB init and data migration
- Docker and Docker Compose
- Google Maps API - https://cloud.google.com/maps-platform/routes/

# Prerequisites
- Docker - Follow instructions https://docs.docker.com/engine/installation/ to install Docker on your machine.
- Google API key - Put your **Google API key** as a value mapped to the key `'google.api.key'`
in `'src/main/resources/application.properties'`

# Build Project
To build the project, run following command:

`./start.sh`

1.  A MySQL DB listening to port `3306` will be created by Docker Compose.
2.  A Spring Docker image will be created by Docker based on the `Dockerfile`.
3.  A `docker-compose-wait` command is added to the Spring Docker image.
4.  The DB initialization will be pending until the MySQL service is ready.
5.  The Spring service will download Gradle for downloading project dependencies 
    and running Gradle commands. It takes a few seconds to download all the files.
6.  `flywayClean`, `flywayBaseline`, `flywayMigrate` will be executed 
    so the SQL files in the `src/main/resources/db.migration` will be 
    executed by Flyway and a table used for Flyway versioning will also be added.
7.  `generateJooq` will be executed to generate all DB related Java files by JOOQ.
8.  `test` will be executed so a Spring boot application testing will start.
9.  `bootRun` will be executed so the application will start with listening to port 8080.

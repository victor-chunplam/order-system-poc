#!/bin/bash
docker stop mysql_lalamove
docker rm mysql_lalamove
docker-compose up -d

EXISTING_MESSAGE=`docker-compose logs -t mysql_lalamove | grep 'mysqld: ready for connections' | tail -1`
NEW_MESSAGE=""
IS_STOP=0
RETRY_TIMES=20
for ((i = 0; IS_STOP < 1 && i < RETRY_TIMES; i++)); do
  NEW_MESSAGE=`docker-compose logs -t mysql_lalamove | grep 'mysqld: ready for connections' | tail -1`

  if [ "$EXISTING_MESSAGE" = "$NEW_MESSAGE" ]; then
    echo "MySQL not yet ready..."
  else
    echo "MySQL is ready"
    echo "$NEW_MESSAGE"
    ./gradlew flywayClean flywayBaseline flywayMigrate generateJooq -Dorg.gradle.java.home="./jdk1.8.0_211.jdk/Contents/Home"
    IS_STOP=1
  fi

  sleep 1
done

if [ "$IS_STOP" = 1 ]; then
  echo "DB is ready! Enjoy!"
  echo "Starting Spring Boot application listening to port 8080 with TESTING......"
  ./gradlew test bootRun -Dorg.gradle.java.home="./jdk1.8.0_211.jdk/Contents/Home"
else
  echo "DB cannot start!"
fi

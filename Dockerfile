FROM openjdk:8-jdk-alpine

RUN mkdir -p /app
WORKDIR /app

RUN apk update && apk add bash

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY gradle-prod.properties .

COPY src src

ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

FROM openjdk:8-jdk-alpine

EXPOSE 8090

ARG JAR_FILE=/target/*.jar

COPY ${JAR_FILE} app.jar

RUN echo "Creating docker image for SpringBoot App"

MAINTAINER "lakshmanandevops@gmail.com"

ENTRYPOINT ["java", "-jar", "app.jar"]
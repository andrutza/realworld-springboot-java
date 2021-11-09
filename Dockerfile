FROM openjdk:14-alpine
COPY build/libs/*.jar /usr/src/webapp/application.jar
WORKDIR /usr/src/webapp
ENTRYPOINT ["java", "-jar", "/usr/src/webapp/application.jar"]

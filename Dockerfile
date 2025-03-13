FROM openjdk:21-jdk-slim
COPY deploy/fitnesspro-service-jar-with-dependencies.jar app.jar
EXPOSE 8080
RUN ls -l /
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:21-jdk-slim
COPY fitnesspro-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
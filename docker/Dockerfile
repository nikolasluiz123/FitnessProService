FROM openjdk:21-jdk-slim
COPY app.jar app.jar
EXPOSE 8081
RUN apt-get update && apt-get install -y --no-install-recommends tzdata && rm -rf /var/lib/apt/lists/*
ENV TZ=America/Sao_Paulo
ENTRYPOINT ["java", "-jar", "app.jar"]
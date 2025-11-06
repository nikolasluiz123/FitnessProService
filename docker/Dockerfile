FROM eclipse-temurin:25-jdk-bookworm
COPY app.jar app.jar
EXPOSE 8081
RUN apt-get update && apt-get install -y --no-install-recommends tzdata && rm -rf /var/lib/apt/lists/*
ENV TZ=Etc/UTC
ENTRYPOINT ["java", "-jar", "app.jar"]
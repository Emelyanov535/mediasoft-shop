FROM openjdk:23-ea-17-jdk-bullseye
WORKDIR /app
COPY build/libs/Shop-0.0.1-SNAPSHOT.jar /app/mediasoft.jar
ENTRYPOINT ["java", "-jar", "mediasoft.jar"]
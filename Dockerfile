
# Etapa 1: Build com Maven e JDK 24
FROM 3.9.11-eclipse-temurin-24-alpine AS build
WORKDIR /loajaApp
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Etapa 2: Imagem final com JRE 24
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
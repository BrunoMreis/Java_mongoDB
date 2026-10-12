# Etapa 1: Build com Maven e JDK 25
FROM maven:3.9.11-eclipse-temurin-25-alpine AS build
WORKDIR /loajaApp
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Etapa 2: Imagem final com JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S -g 10001 app && adduser -S -u 10001 -G app app
COPY --from=build --chown=app:app /loajaApp/target/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
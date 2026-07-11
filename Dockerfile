FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml pom.xml
COPY search-engine-core search-engine-core
COPY search-engine-server search-engine-server
RUN mvn -pl search-engine-server -am package -DskipTests

FROM node:24-alpine AS frontend-build
WORKDIR /app
COPY search-engine-web/package*.json ./
RUN npm ci
COPY search-engine-web ./
RUN npm run build

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/search-engine-server/target/*.jar app.jar
COPY --from=frontend-build /app/dist ./static
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

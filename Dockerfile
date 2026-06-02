# Étape 1 : Compilation du projet avec Maven et JDK 24
FROM maven:3.9.9-amazoncorretto-24 AS build
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM amazoncorretto:24
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

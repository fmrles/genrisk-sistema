
# ---- Esta corresponde a la etapa de compilación del proyecto -----

FROM maven:3.9-eclipse-temurin-21 AS build 

WORKDIR /app

COPY pom.xmL .

COPY src ./src

RUN mvn clean package -DskipTests

# ------------------------------------------------------------------ 


# ----- Esta es la etapa de ejecución (completa) ------

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 

# ------------------------------------------------------
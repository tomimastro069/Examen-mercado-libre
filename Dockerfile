# ---- Etapa 1: Compilar el proyecto ----
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

COPY . /app

# Compila y genera el JAR (sin tests para build más rápido)
RUN gradle clean build -x test

# ---- Etapa 2: Imagen final ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiamos el JAR generado
COPY --from=builder /app/build/libs/*.jar app.jar

# Puerto que expone tu app (cambiar si usás otro)
EXPOSE 8080

# Comando para ejecutar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]

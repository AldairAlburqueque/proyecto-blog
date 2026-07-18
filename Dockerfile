# ==========================================
# ETAPA 1: Compilación y empaquetado (Build) (Cambiado a Java 21)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Definimos el directorio de trabajo dentro del contenedor para compilar
WORKDIR /app

# Copiamos el archivo de configuración de dependencias (pom.xml)
COPY pom.xml .

# Descargamos las dependencias de Maven sin compilar el código aún (esto aprovecha el caché de Docker)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente de nuestra aplicación
COPY src ./src

# Compilamos y empaquetamos el proyecto omitiendo las pruebas unitarias para acelerar el proceso
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Entorno de ejecución (Run) (Cambiado a Java 21)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos únicamente el archivo .jar generado en la etapa anterior (la etapa "build")
# Asegúrate de que el nombre del .jar coincida con el generado en tu pom.xml (por defecto suele ser name-version.jar)
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre tu aplicación Spring Boot (usualmente el 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
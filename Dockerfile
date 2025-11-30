# Usar una imagen base ligera con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado desde Maven
COPY target/*.jar app.jar

# Exponer el puerto 8086
EXPOSE 8086

# Configurar el puerto del servidor Spring Boot
ENV SERVER_PORT=8086

# Punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-Dserver.port=8086", "-jar", "app.jar"]
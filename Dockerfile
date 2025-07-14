# Contrucción del archivo .jar
FROM maven:3-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Contrucción de la imagen final
FROM amazoncorretto:21
WORKDIR /app

# Copiar el archivo .jar generado desde el build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Activar el perfil de producción por defecto
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar la aplicación
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
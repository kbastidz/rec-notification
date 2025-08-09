FROM openjdk:17-jre-slim
COPY tu-aplicacion.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

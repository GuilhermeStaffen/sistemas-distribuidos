FROM openjdk
WORKDIR /app
COPY target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar /app/todolistapi.jar
ENTRYPOINT ["java", "-jar", "todolistapi.jar"]
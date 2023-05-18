FROM openjdk:19-jdk-alpine
COPY ./domain-service-svc/target/domain-service-svc-0.0.1-SNAPSHOT.jar health-check-domain-service-0.0.1.jar
EXPOSE 8080 8050
ENTRYPOINT ["java", "-Dspring.profiles.active=dev","-jar", "health-check-domain-service-0.0.1.jar"]
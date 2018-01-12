FROM java:8-jre

ADD ./target/configuration-service-1.0.jar /app/

CMD ["java", "-Dserver.port=8888", "-jar", "/app/configuration-service-1.0.jar"]

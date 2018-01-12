FROM java:8-jre

ADD ./target/registry-service-1.0.jar /app/

CMD ["java", "-Xmx200m", "-Dserver.port=8989", "-jar", "/app/registry-service-1.0.jar"]

EXPOSE 8989
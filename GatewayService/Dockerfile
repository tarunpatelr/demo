FROM java:8-jre

ADD ./target/gateway-service-1.0.jar /app/

CMD ["java", "-Xmx200m", "-Dserver.port=4000", "-jar", "/app/gateway-service-1.0.jar"]

EXPOSE 4000
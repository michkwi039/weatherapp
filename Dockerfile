FROM openjdk:11
ADD target/processor.jar processor.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "processor.jar"]
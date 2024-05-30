FROM openjdk:17
WORKDIR /app
ADD target/chat-back-end-0.0.1-SNAPSHOT.jar springboot-docker.jar
ENTRYPOINT ["java", "-jar", "springboot-docker.jar"]
EXPOSE 8080

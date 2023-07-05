FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
#RUN rm -rf /var/lib/apt/lists/*
#ENTRYPOINT ["java", "-jar","/app.jar"]
ENTRYPOINT ["java", "-Dspring.config.use-legacy-processing=true","-jar","/app.jar", "ffmpeg"]
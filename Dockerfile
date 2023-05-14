FROM maven:3 as builder

WORKDIR /app
COPY . .
RUN mvn -Dmaven.test.skip=true package 

FROM openjdk:17-jdk

WORKDIR /home/app
RUN useradd -ms /bin/bash -u 999 app
USER app
COPY --from=builder /app/target/*.jar runner.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "runner.jar"]
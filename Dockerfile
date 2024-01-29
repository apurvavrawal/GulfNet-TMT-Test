FROM gradle:8.4.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#RUN gradle build --no-daemon

#FROM openjdk:8-jre-slim

EXPOSE 8084

RUN mkdir /app
#WORKDIR /app
COPY . .
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/
RUN ls
RUN gradle build  --no-daemon
CMD gradle  bootRun
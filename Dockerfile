FROM openjdk:11

MAINTAINER sh

WORKDIR /home/three-sentences

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /home/three-sentences/app.jar

EXPOSE 8080
EXPOSE 9292

CMD ["java", "-Xm128M", "-Xmx256M", "-jar", "/home/three-sentences/app.jar"]

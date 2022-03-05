FROM adoptopenjdk/openjdk11:latest
LABEL maintainer="Surya"

COPY config.yml /opt/tasker-dw/
COPY tasker.yml /opt/tasker-dw/
COPY target/tasker-dw-1.0-SNAPSHOT.jar /opt/tasker-dw/tasker-dw.jar

EXPOSE 9000 9001

WORKDIR /opt/tasker-dw

CMD ["java", "-jar", "-Done-jar.silent=true", "tasker-dw.jar", "server", "tasker.yml"]
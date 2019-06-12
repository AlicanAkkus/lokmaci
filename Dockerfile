FROM openjdk:11-jre-slim-sid
LABEL maintainer=aakkus

COPY build/libs/lokmaci-0.0.1-SNAPSHOT.jar lib/lokmaci.jar

ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar lib/lokmaci.jar
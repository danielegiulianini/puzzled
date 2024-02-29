FROM gradle:8-jdk-alpine

#should copy only the source code this project depends on
COPY --chown=gradle:gradle ../../ /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle run :puzzleapigateway --no-daemon

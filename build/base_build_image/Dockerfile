FROM hseeberger/scala-sbt:8u181_2.12.7_1.2.3 AS build

COPY /build.sbt /usr/src/gymio-service/build.sbt
COPY /project/plugins.sbt /usr/src/gymio-service/project/plugins.sbt

WORKDIR /usr/src/gymio-service

RUN ["sbt", "compile"]

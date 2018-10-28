FROM spikerlabs/scala-sbt:scala-2.12.7-sbt-1.2.6

COPY /build.sbt /usr/src/gymio-service/build.sbt
COPY /project/plugins.sbt /usr/src/gymio-service/project/plugins.sbt

WORKDIR /usr/src/gymio-service

RUN ["sbt", "compile"]
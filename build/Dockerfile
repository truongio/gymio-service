FROM spikerlabs/scala-sbt:scala-2.12.7-sbt-1.2.6 AS build

ADD /src /usr/src/gymio-service/src
COPY /build.sbt /usr/src/gymio-service/build.sbt
COPY /project/build.properties /usr/src/gymio-service/project/build.properties
COPY /project/plugins.sbt /usr/src/gymio-service/project/plugins.sbt

WORKDIR /usr/src/gymio-service

RUN ["sbt", "universal:packageZipTarball"]

FROM openjdk:8-jre-alpine3.8

COPY --from=build /usr/src/gymio-service/target/universal/gymio*.tgz .

RUN tar -xvzf gymio*.tgz
RUN rm gymio-*.tgz

ENTRYPOINT ["/bin/sh", "-c", "/gymio-*/bin/gymio-service"]

CMD []

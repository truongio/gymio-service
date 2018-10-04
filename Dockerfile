FROM hseeberger/scala-sbt:8u181_2.12.7_1.2.3

ADD /src /usr/src/gymio-service/src
COPY /project/build.properties /usr/src/gymio-service/project/build.properties
COPY /project/plugins.sbt /usr/src/gymio-service/project/plugins.sbt
COPY /build.sbt /usr/src/gymio-service/build.sbt

WORKDIR /usr/src/gymio-service

EXPOSE 8080

RUN sbt compile

ENTRYPOINT ["sbt", "run"]
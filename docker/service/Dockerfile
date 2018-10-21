FROM hseeberger/scala-sbt:8u181_2.12.7_1.2.3

ADD /src /usr/src/gymio-service/src
COPY /build.sbt /usr/src/gymio-service/build.sbt

WORKDIR /usr/src/gymio-service

EXPOSE 8080

RUN sbt compile

ENTRYPOINT ["sbt", "run"]
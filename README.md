[![CircleCI](https://circleci.com/gh/truongio/gymio-service/tree/master.svg?style=shield)](https://circleci.com/gh/truongio/gymio-service/tree/master)
# Gymio

### Requirements

* `java`
* `scala`
* `sbt` (https://www.scala-sbt.org/index.html)

### Run

To start HTTP server, do
```
sbt run
```
### Run in Docker
* `docker build -t gymio:latest .`
* `docker run -it -p 8080:8080 gymio:latest`

### End points

Method | Url                                 | Description
------ | ----------------------------------- | -----------
GET    | /workout/{userId}                   | All workouts
GET    | /workout/active/{userId}            | Active workout
POST   | /workout/active/{userId}/start      | Start a workout
POST   | /workout/active/{userId}/log        | Log a completed exercise to an active workout
POST   | /workout/active/{userId}/finish     | Finish an active workout
GET    | /user-stats/{userId}                | Stats for user
POST   | /user-stats/{userId}                | Save stats for user


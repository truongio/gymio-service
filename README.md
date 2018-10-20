# Pre requirements

* `java`
* `scala`
* `sbt` (https://www.scala-sbt.org/index.html)

# Gymio

To start HTTP server, do
```
sbt run
```
# Run in Docker
* `docker build -t gymio:latest .`
* `docker run -it -p 8080:8080 gymio:latest`

## End points

Method | Url                             | Description
------ | ------------------------------- | -----------
GET    | /workout/{id}                   | All workouts
GET    | /workout/active/{id}            | Active workout
POST   | /workout/active/{id}/start      | Start a workout
POST   | /workout/active/{id}/log        | Log a completed exercise to an active workout
POST   | /workout/active/{id}/finish     | Finish an active workout


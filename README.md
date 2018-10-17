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

# Example routes

GET `http://localhost:8080/workout/84a07f4e-85e6-4d75-a983-e6107eeb5f2a/start`

GET `http://127.0.0.1:8080/workout/active/84a07f4e-85e6-4d75-a983-e6107eeb5f2a`

POST `http://127.0.0.1:8080/workout/84a07f4e-85e6-4d75-a983-e6107eeb5f2a/log`

POST `http://127.0.0.1:8080/workout/84a07f4e-85e6-4d75-a983-e6107eeb5f2a/complete`

GET `http://127.0.0.1:8080/workout/84a07f4e-85e6-4d75-a983-e6107eeb5f2a`


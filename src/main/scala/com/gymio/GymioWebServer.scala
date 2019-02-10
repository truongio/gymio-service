package com.gymio

import cats.effect._
import cats.implicits._
import com.gymio.db.{UserStatsDoobieRepo, WorkoutDoobieRepo}
import com.typesafe.config.{Config, ConfigFactory}
import doobie.implicits._
import org.flywaydb.core.Flyway
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware._

import scala.concurrent.duration._

object GymioWebServer extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val conf = ConfigFactory.load()

    migrateDb(conf)

    val doobieRepo = new WorkoutDoobieRepo(DatabaseDoobie.xa)
    val userStatsRepo = new UserStatsDoobieRepo(DatabaseDoobie.xa)

    val methodConfig = CORSConfig(
      anyOrigin = true,
      anyMethod = false,
      allowedMethods = Some(Set("GET", "POST")),
      allowCredentials = true,
      maxAge = 1.day.toSeconds
    )

    val httpApp =
      Router(
        WorkoutAPI.root   -> CORS(new WorkoutAPI(doobieRepo).workoutAPI, methodConfig),
        UserStatsAPI.root -> CORS(new UserStatsAPI(userStatsRepo).userStatsAPI, methodConfig),
        "/"               -> CORS(HealthCheckAPI.healthCheckAPI)
      ).orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

  private def migrateDb(config: Config): Int = {
    val flyway = new Flyway()

    val url = config.getString("database.url")
    val user = config.getString("database.user")
    val password = config.getString("database.password")

    flyway.setDataSource(url, user, password)
    flyway.migrate
  }
}

package com.gymio

import cats.effect._
import cats.implicits._
import com.gymio.db.common.DatabaseProfile.api._
import com.gymio.db.{UserStatsPSQLRepo, WorkoutPGSQLRepo}
import com.typesafe.config.{Config, ConfigFactory}
import io.circe.generic.auto._
import org.flywaydb.core.Flyway
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{CORS, _}

import scala.concurrent.duration._

object GymioWebServer extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val conf          = ConfigFactory.load()
    val db            = Database.forConfig("slick.db")
    val repo          = new WorkoutPGSQLRepo(db)
    val userStatsRepo = new UserStatsPSQLRepo(db)

    migrateDb(conf)

    val methodConfig = CORSConfig(
      anyOrigin = true,
      anyMethod = false,
      allowedMethods = Some(Set("GET", "POST")),
      allowCredentials = true,
      maxAge = 1.day.toSeconds)

    val httpApp =
      Router(
        WorkoutAPI.root   -> CORS(new WorkoutAPI(repo).workoutAPI, methodConfig),
        UserStatsAPI.root -> CORS(new UserStatsAPI(userStatsRepo).userStatsAPI, methodConfig)
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

    val url      = config.getString("slick.db.url")
    val user     = config.getString("slick.db.user")
    val password = config.getString("slick.db.password")

    flyway.setDataSource(url, user, password)
    flyway.migrate
  }
}

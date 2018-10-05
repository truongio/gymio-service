package com.gymio

import java.util.UUID

import cats.effect._
import cats.implicits._
import com.gymio.domain.model._
import com.gymio.domain.service.ExerciseLogService.logExercise
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.blaze.BlazeServerBuilder

object GymioWebServer extends IOApp {

  var log: Map[UUID, ExerciseLog] = Map()

  private val jsonService = HttpRoutes
    .of[IO] {
      case GET -> Root / "log" =>
        Ok(log.asJson)
      case req @ POST -> Root / "log" / UUIDVar(userId) / "add" =>
        for {
          e <- req.as[Event]
          res <- {
            log += userId -> log
              .get(userId)
              .fold(ExerciseLog(List(e)))(logExercise(e))
            Ok(log.asJson)
          }
        } yield res
    }
    .orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(jsonService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

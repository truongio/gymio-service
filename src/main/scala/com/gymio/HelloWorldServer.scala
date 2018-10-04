package com.gymio

import java.util.UUID

import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import com.gymio.HelloWorldServer.log
import com.gymio.domain.model._
import com.gymio.domain.service.ExerciseLogService
import com.gymio.domain.service.ExerciseLogService.logCompletedExercise
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.blaze.{BlazeBuilder, BlazeServerBuilder}

object HelloWorldServer extends IOApp {

  implicit val decoder: EntityDecoder[IO, Event] = jsonOf[IO, Event]
  var log = ExerciseLog(UUID.randomUUID, List(BenchSetCompleted(5, Weight(50, Kg))))

  private val jsonService = HttpRoutes.of[IO] {
    case GET -> Root / "log" =>
      Ok(log.asJson)
    case req@POST -> Root / "log" / "add" =>
      for {
        e <- req.as[Event]
        res <- {
          log = logCompletedExercise(e)(log)
          Ok(log.asJson)
        }
      } yield res
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(jsonService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

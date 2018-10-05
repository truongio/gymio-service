package com.gymio

import java.util.UUID

import cats.data.Kleisli
import cats.effect._
import com.gymio.domain.model._
import com.gymio.domain.service.ExerciseLogService.logExercise
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class GymioService {
  var log: Map[UUID, ExerciseLog] = Map()

  val gymioService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "log" =>
        Ok(log.asJson)
      case req @ POST -> Root / "log" / UUIDVar(userId) / "add" =>
        logExerciseForUser(req, userId)
    }
    .orNotFound

  private def logExerciseForUser(req: Request[IO], userId: UUID) = {
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
}

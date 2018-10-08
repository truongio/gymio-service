package com.gymio

import java.util.UUID
import cats.data.Kleisli
import cats.effect._
import com.gymio.domain.model._
import com.gymio.domain.service.ExerciseLogService
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}
import io.circe.generic.auto._
import io.circe.syntax._

class GymioService {
  var log: Map[UUID, ExerciseLog] = Map()
  var eventStore: Seq[Event]      = List()

  val gymioService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "log" =>
        Ok(log.asJson)
      case req @ POST -> Root / "log" / UUIDVar(userId) / "add" =>
        logExerciseForUser(req, userId)
    }
    .orNotFound

  private def logExerciseForUser(req: Request[IO],
                                 userId: UUID): IO[Response[IO]] = {
    for {
      c    <- req.as[Command]
      eLog <- IO(log.get(userId).getOrElse(ExerciseLog(List())))
      e    <- IO.fromEither(ExerciseLogService.decide(c)(eLog))
      _    <- updateStore(e)
      _    <- updateLog(userId, e, eLog)
      res  <- Ok(log.asJson)
    } yield res
  }

  private def updateLog(
      userId: UUID,
      event: Event,
      exerciseLog: ExerciseLog): IO[Map[UUID, ExerciseLog]] = {
    log += userId -> ExerciseLogService.applyEvent(event)(exerciseLog)

    IO(log)
  }

  private def updateStore(event: Event): IO[Seq[Event]] = {
    eventStore = eventStore :+ event
    IO(eventStore)
  }

}

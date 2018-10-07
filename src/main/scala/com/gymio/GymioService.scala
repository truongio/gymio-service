package com.gymio

import java.util.UUID

import cats.data.Kleisli
import cats.effect._
import com.gymio.domain.model._
import com.gymio.domain.service.ExerciseLogService
import com.gymio.domain.service.ExerciseLogService.decide
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class GymioService {
  var log: Map[UUID, ExerciseLog] = Map()
  var eventStore: Seq[Event] = List()

  val gymioService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "log" =>
        Ok(log.asJson)
      case req @ POST -> Root / "log" / UUIDVar(userId) / "add" =>
        logExerciseForUser(req, userId)
    }
    .orNotFound


  private def logExerciseForUser(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    for {
      c <- req.as[Command]
      _ <- updateStore(c, userId)
      _ <- updateLog(c, userId)
      res <- Ok(log.asJson)
    } yield res
  }

  private def updateLog(c: Command, userId: UUID): IO[Map[UUID, ExerciseLog]] = {
    val exerciseLog = log.get(userId).getOrElse(ExerciseLog(List()))
    for (e <- decide(c)(exerciseLog)) {
      log += userId -> ExerciseLogService.applyEvent(e)(exerciseLog)
    }

    IO(log)
  }

  private def updateStore(c: Command, userId: UUID): IO[Seq[Event]] = {
    val exerciseLog = log.get(userId).getOrElse(ExerciseLog(List()))
    for (e <- decide(c)(exerciseLog)) {
      eventStore = eventStore :+ e
    }

    IO(eventStore)
  }

}

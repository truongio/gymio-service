package com.gymio

import java.util.UUID
import java.util.UUID.randomUUID

import cats.data.Kleisli
import cats.effect.IO.{fromEither, fromFuture, pure}
import cats.effect._
import com.gymio.domain.infrastructure.WorkoutRepo
import com.gymio.domain.model.Status.Active
import com.gymio.domain.model._
import com.gymio.domain.service.WorkoutService
import com.gymio.domain.service.WorkoutService.{decide, nextWorkout}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class WorkoutAPI(repo: WorkoutRepo) {
  val workoutAPI: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "workout" / UUIDVar(userId) =>
        workouts(userId)

      case GET -> Root / "workout" / "active" / UUIDVar(userId) =>
        getActiveWorkout(userId)

      case POST -> Root / "workout" / "active" / UUIDVar(userId) / "start" =>
        startWorkout(userId)

      case req @ POST -> Root / "workout" / "active" / UUIDVar(userId) / "log" =>
        logExerciseForWorkout(req, userId)

      case req @ POST -> Root / "workout" / "active" / UUIDVar(userId) / "finish" =>
        completeWorkout(req, userId)
    }
    .orNotFound

  def workouts(userId: UUID): IO[Response[IO]] = {
    val f = repo find userId
    for {
      ws  <- fromFuture(pure(f))
      res <- Ok(ws.asJson)
    } yield res

  }

  def getActiveWorkout(userId: UUID): IO[Response[IO]] = {
    val f = repo find userId
    for {
      ws <- fromFuture(pure(f))
      res <- ws
        .filter(_.status == Active)
        .lastOption
        .map(w => Ok(w.asJson))
        .getOrElse(NoContent())
    } yield res
  }

  def startWorkout(userId: UUID): IO[Response[IO]] = {
    val f        = repo find userId
    val defaultW = Workout(randomUUID, userId, Active, 1, 1, List())
    for {
      ws <- fromFuture(pure(f))
      w = ws.lastOption.map(nextWorkout).getOrElse(defaultW)
      _   <- saveWorkout(userId)(w)
      res <- Ok(w.asJson)
    } yield res
  }

  def logExerciseForWorkout(req: Request[IO],
                            userId: UUID): IO[Response[IO]] = {
    val f = repo find userId
    for {
      ws <- fromFuture(pure(f))
      w = ws.filter(_.status == Active).last
      c     <- req.as[Command]
      e     <- fromEither(decide(c))
      saved <- saveWorkout(userId)(WorkoutService.apply(e)(w))
      res   <- Accepted(saved.asJson)
    } yield res
  }

  def completeWorkout(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    val f = repo find userId
    for {
      ws <- fromFuture(pure(f))
      w = ws.filter(_.status == Active).last.copy(status = Status.Completed)
      _   <- saveWorkout(userId)(w)
      res <- Accepted(w.asJson)
    } yield res
  }

  def saveWorkout(userId: UUID)(w: Workout): IO[Workout] = {
    val savedWorkout = repo.save(userId, w)
    fromFuture(pure(savedWorkout))
  }
}

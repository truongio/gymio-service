package com.gymio

import java.util.UUID
import java.util.UUID.randomUUID

import cats.effect.IO.fromEither
import cats.effect._
import com.gymio.db.WorkoutDoobieRepo
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

object WorkoutAPI {
  val root = "/workout"
}

class WorkoutAPI(doobieRepo: WorkoutDoobieRepo) {

  val workoutAPI: HttpRoutes[IO] = HttpRoutes
    .of[IO] {
      case GET -> Root / UUIDVar(userId) =>
        workouts(userId)

      case GET -> Root / "active" / UUIDVar(userId) =>
        getActiveWorkout(userId)

      case POST -> Root / "active" / UUIDVar(userId) / "start" =>
        startWorkout(userId)

      case req @ POST -> Root / "active" / UUIDVar(userId) / "log" =>
        println(req)
        logExerciseForWorkout(req, userId)

      case req @ POST -> Root / "active" / UUIDVar(userId) / "finish" =>
        completeWorkout(req, userId)
    }

  def workouts(userId: UUID): IO[Response[IO]] = {
    for {
      ws <- doobieRepo.find(userId)
      r  <- Ok(ws.asJson)
    } yield r
  }

  def getActiveWorkout(userId: UUID): IO[Response[IO]] = {
    for {
      ws <- doobieRepo.find(userId)
      r <- ws
        .flatMap(WorkoutDoobieRepo.toWorkout)
        .filter(_.status == Active)
        .lastOption
        .map(w => Ok(w.asJson))
        .getOrElse(NoContent())
    } yield r
  }

  def startWorkout(userId: UUID): IO[Response[IO]] = {
    val defaultW = Workout(randomUUID, userId, Active, 1, 1, List())
    for {
      ws <- doobieRepo.find(userId)
      w = ws.flatMap(WorkoutDoobieRepo.toWorkout).lastOption.map(nextWorkout).getOrElse(defaultW)
      _ <- doobieRepo.save(userId, w)
      r <- Ok(w.asJson)
    } yield r
  }

  def logExerciseForWorkout(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    for {
      ws <- doobieRepo.find(userId)
      w = ws.flatMap(WorkoutDoobieRepo.toWorkout).filter(_.status == Active).last
      c <- req.as[CompleteExercise]
      e <- fromEither(decide(c))
      _ <- doobieRepo.save(userId, WorkoutService.apply(e)(w))
      r <- Accepted(w.asJson)
    } yield r
  }

  def completeWorkout(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    for {
      ws <- doobieRepo.find(userId)
      w = ws.flatMap(WorkoutDoobieRepo.toWorkout).filter(_.status == Active).last.copy(status = Status.Completed)
      _ <- doobieRepo.save(userId, w)
      r <- Accepted(w.asJson)
    } yield r
  }

}

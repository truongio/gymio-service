package com.gymio

import java.util.UUID
import cats.data.Kleisli
import cats.effect._
import com.gymio.domain.model._
import com.gymio.domain.service.WorkoutService
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}
import io.circe.generic.auto._
import io.circe.syntax._

class GymioService {
  var workoutStore: Map[UUID, Seq[Workout]] = Map()
  var activeWorkout: Map[UUID, Workout]     = Map()

  val gymioService: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "workout" / UUIDVar(userId) =>
        getActiveWorkout(userId)
      case req @ POST -> Root / "workout" / UUIDVar(userId) / "start" =>
        startWorkout(userId)
      case req @ POST -> Root / "workout" / UUIDVar(userId) / "log" =>
        logExerciseForWorkout(req, userId)
      case req @ POST -> Root / "workout" / UUIDVar(userId) / "complete" =>
        completeWorkout(req, userId)
  }
    .orNotFound

  def getActiveWorkout(userId: UUID): IO[Response[IO]] = {
    activeWorkout.get(userId).map(w => Ok(w.asJson)).getOrElse(NotFound())
  }

  def startWorkout(userId: UUID): IO[Response[IO]] = {
    val lastWorkout = workoutStore.getOrElse(userId, List(Workout(1, userId, 3, 0, List()))).last
    activeWorkout = Map(userId -> WorkoutService.getNextWorkout(lastWorkout))
    Accepted(activeWorkout.asJson)
  }

  def logExerciseForWorkout(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    activeWorkout.get(userId).map { w =>
      for {
        c   <- req.as[Command]
        e   <- IO.fromEither(WorkoutService.decide(c))
        _   <- updateActiveWorkout(userId, e)(w)
        res <- Accepted(activeWorkout.asJson)
      } yield res
    }.getOrElse(NotFound())
  }


  def updateActiveWorkout(userId: UUID, event: Event)(w: Workout): IO[Map[UUID, Workout]] = {
    activeWorkout += userId -> WorkoutService.applyEvent(event)(w)
    IO(activeWorkout)
  }

  def updateWorkoutStore(userId: UUID): Unit = {
    activeWorkout.get(userId) foreach { s =>
      workoutStore += userId -> (workoutStore.getOrElse(userId, List()) :+ s)
    }
  }

  def completeWorkout(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    updateWorkoutStore(userId)
    activeWorkout -= userId
    Accepted(workoutStore.asJson)
  }
}

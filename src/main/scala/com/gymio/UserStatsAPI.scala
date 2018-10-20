package com.gymio

import java.util.UUID

import cats.data.Kleisli
import cats.effect._
import com.gymio.domain.infrastructure.UserStatsRepo
import com.gymio.domain.model.UserStats
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

class UserStatsAPI(repo: UserStatsRepo) {
  val userStatsAPI: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "user-stats" / UUIDVar(userId) =>
        getUserStats(userId)

      case req @ POST -> Root / "user-stats" / UUIDVar(userId) =>
        saveUserStats(req, userId)
    }
    .orNotFound

  def getUserStats(userId: UUID): IO[Response[IO]] = {
    for {
      res      <- IO.fromFuture(IO.pure(repo.find(userId)))
      response <- Ok(res.asJson)
    } yield response
  }

  def saveUserStats(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    for {
      c   <- req.as[UserStats]
      s   <- IO.fromFuture(IO.pure(repo.save(userId, c)))
      res <- Ok(s.asJson)
    } yield res
  }
}

package com.gymio

import java.util.UUID

import cats.effect._
import com.gymio.db.UserStatsDoobieRepo
import com.gymio.domain.model.UserStats
import io.circe.syntax._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{HttpRoutes, Request, Response}

object UserStatsAPI {
  val root = "/user-stats"
}

class UserStatsAPI(repo: UserStatsDoobieRepo) {
  val userStatsAPI: HttpRoutes[IO] = HttpRoutes
    .of[IO] {
    case GET -> Root / UUIDVar(userId) =>
      getUserStats(userId)

    case req @ POST -> Root / UUIDVar(userId) =>
      saveUserStats(req, userId)
  }

  def getUserStats(userId: UUID): IO[Response[IO]] = {
    for {
      uStatsL <- repo.find(userId)
      r       <- uStatsL.headOption.map(us => Ok(us.asJson)).getOrElse(NoContent())
    } yield r
  }

  def saveUserStats(req: Request[IO], userId: UUID): IO[Response[IO]] = {
    for {
      us <- req.as[UserStats]
      s  <- repo.save(userId, us)
      r  <- Accepted(s.asJson)
    } yield r
  }
}

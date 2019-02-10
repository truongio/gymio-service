package com.gymio.db
import java.time.Instant
import java.time.Instant.now
import java.util.UUID

import cats.data.NonEmptyList
import cats.effect.IO
import cats.implicits._
import com.gymio.domain.model.UserStats
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor
import io.circe.Json
import io.circe.syntax._

class UserStatsDoobieRepo(transactor: Transactor[IO]) {
  def find(userId: UUID) = {
    sql"SELECT * from user_stats"
      .query[UserStatsDoobieRepo.UserStatsRecord]
      .unique
      .map(UserStatsDoobieRepo.toUserStats)
      .transact(transactor)
  }

  def save(userId: UUID, us: UserStats): IO[Int] = {
    val r = UserStatsDoobieRepo.toRecord(userId, us)
    sql"""
         INSERT INTO user_stats (id, data, timestamp) 
         VALUES (${r.id}, ${r.data}, ${r.timestamp})
         ON CONFLICT (id)
         DO UPDATE
         SET data = ${r.data},
             timestamp = ${r.timestamp}
      """.update.run
      .transact(transactor)
  }
}

object UserStatsDoobieRepo {
  case class UserStatsRecord(id: UUID, data: Json, timestamp: Instant)

  def toRecord(userId: UUID, w: UserStats): UserStatsRecord = {
    UserStatsRecord(userId, w.asJson, now)
  }

  def toUserStats(w: UserStatsRecord): Seq[UserStats] = {
    w.data.as[UserStats].toSeq
  }
}

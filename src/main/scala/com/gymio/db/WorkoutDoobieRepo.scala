package com.gymio.db
import java.time.Instant
import java.time.Instant.now
import java.util.UUID

import cats._
import cats.data.{NonEmptyList, _}
import cats.effect.IO
import cats.implicits._
import com.gymio.domain.model.Workout
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.postgresql.util.PGobject

class WorkoutDoobieRepo(transactor: Transactor[IO]) {
  def find(userId: UUID) = {
    sql"SELECT * from workout"
      .query[WorkoutDoobieRepo.WorkoutRecord]
      .stream
      .take(5)
      .compile.toList
      .transact(transactor)
  }

  def save(userId: UUID, w: Workout): IO[Int] = {
    val record = WorkoutDoobieRepo.toRecord(userId, w)
    sql"""
         INSERT into workout (id, user_id, data, timestamp)
         VALUES (${record.id}, $userId, ${record.data}, ${record.timestamp})
         ON CONFLICT (id)
         DO UPDATE
         SET id = ${record.id},
             user_id = $userId,
             data = ${record.data},
             timestamp = ${record.timestamp}
      """
      .update
      .run
      .transact(transactor)

  }
}

object WorkoutDoobieRepo {
  case class WorkoutRecord(
    id: UUID,
    userId: UUID,
    data: Json,
    timestamp: Instant
  )

  def toRecord(userId: UUID, w: Workout): WorkoutRecord = {
    WorkoutRecord(w.id, userId, w.asJson, now)
  }

  def toWorkout(w: WorkoutRecord): List[Workout] = {
    w.data.as[Workout].toList
  }
}

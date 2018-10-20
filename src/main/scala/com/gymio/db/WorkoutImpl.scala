package com.gymio.db

import java.time.Instant
import java.time.Instant.now
import java.util.UUID

import com.gymio.db.WorkoutRecord.toRecord
import com.gymio.db.common.DatabaseProfile.api._
import com.gymio.db.common.DatabaseProfileProvider
import com.gymio.domain.infrastructure.WorkoutRepo
import com.gymio.domain.model.Workout
import io.circe.Json
import io.circe.syntax._
import io.strongtyped.active.slick.Lens._
import io.strongtyped.active.slick.{ActiveRecord, EntityActions, Lens}
import slick.ast.BaseTypedType
import slick.lifted.{Rep, TableQuery, Tag}

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object WorkoutActions extends EntityActions with DatabaseProfileProvider {

  override val baseTypedType = implicitly[BaseTypedType[Id]]

  override type Entity      = WorkoutRecord
  override type Id          = UUID
  override type EntityTable = WorkoutTable

  override def tableQuery: TableQuery[EntityTable] = TableQuery[WorkoutTable]

  override def $id(table: EntityTable): Rep[Id] = table.id

  override def idLens: Lens[Entity, Option[Id]] = {
    lens { r: WorkoutRecord => Option(r.id)
    } { (r, id) => r.copy(id = id.get)
    }
  }

}

class WorkoutPGSQLRepo(db: Database) extends WorkoutRepo {

  implicit class EntryExtensions(val model: WorkoutRecord)
      extends ActiveRecord(WorkoutActions)

  import WorkoutActions._

  def save(userId: UUID, l: Workout): Future[Workout] = {
    db.run(WorkoutActions.findOptionById(l.id))
      .flatMap {
        case Some(_) => db.run(update(toRecord(userId, l)))
        case None    => db.run(insert(toRecord(userId, l)))
      }
      .map(_ => l)
  }

  def find(userId: UUID): Future[Seq[Workout]] = {
    val tableQuery = TableQuery[WorkoutTable]
    val q          = tableQuery.filter(_.userId === userId).result
    val result     = db.run(q)

    result.map(_.flatMap(WorkoutRecord.toWorkout))
  }
}

class WorkoutTable(tag: Tag) extends Table[WorkoutRecord](tag, "workout") {

  def id: Rep[UUID]     = column("id", O.PrimaryKey)
  def userId: Rep[UUID] = column("user_id")
  def data: Rep[Json]   = column("data")
  def timestamp: Rep[Instant] = column("timestamp")

  override def * =
    (id, userId, data, timestamp) <> ((WorkoutRecord.apply _).tupled, WorkoutRecord.unapply)
}

case class WorkoutRecord(
    id: UUID,
    userId: UUID,
    data: Json,
    timestamp: Instant
)

object WorkoutRecord {
  def toRecord(userId: UUID, w: Workout): WorkoutRecord = {
    WorkoutRecord(w.id, userId, w.asJson, now)
  }

  def toWorkout(w: WorkoutRecord): immutable.Seq[Workout] = {
    w.data.as[Workout].toSeq
  }
}

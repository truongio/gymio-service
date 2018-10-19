package com.gymio.domain.infrastructure

import java.util.UUID

import com.gymio.domain.infrastructure.common.DatabaseProfile.api._
import com.gymio.domain.infrastructure.common.DatabaseProfileProvider
import com.gymio.domain.model.Workout
import io.circe.Decoder.Result
import io.circe.Json
import io.circe.syntax._
import io.strongtyped.active.slick.Lens._
import io.strongtyped.active.slick.{ActiveRecord, EntityActions, Lens}
import slick.ast.BaseTypedType
import slick.lifted.{Rep, TableQuery, Tag}

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
    lens { r: WorkoutRecord => Option(r.listingId)
    } { (r, id) => r.copy(listingId = id.get)
    }
  }

}

class WorkoutPGSQLRepo(db: Database) extends WorkoutRepo {

  implicit class EntryExtensions(val model: WorkoutRecord)
      extends ActiveRecord(WorkoutActions)

  import WorkoutActions._

  def save(l: Workout): Future[Workout] = {
    val query = insert(WorkoutRecord.toRecord(l))
    db.run(query.transactionally).map(_ => l)
  }

  def find(id: UUID): Future[Seq[Result[Workout]]] = {
    val tableQuery = TableQuery[WorkoutTable]
    val q          = tableQuery.filter(_.id === id).result
    val result     = db.run(q)

    result.map(_.map(WorkoutRecord.toWorkout))
  }
}

class WorkoutTable(tag: Tag) extends Table[WorkoutRecord](tag, "workout") {

  def id: Rep[UUID]   = column("id", O.PrimaryKey)
  def data: Rep[Json] = column("data")

  override def * =
    (id, data) <> ((WorkoutRecord.apply _).tupled, WorkoutRecord.unapply)
}

case class WorkoutRecord(
    listingId: UUID,
    data: Json
)

object WorkoutRecord {
  def toRecord(w: Workout): WorkoutRecord = {
    WorkoutRecord(w.id, w.asJson)
  }

  def toWorkout(w: WorkoutRecord): Result[Workout] = {
    w.data.as[Workout]
  }
}

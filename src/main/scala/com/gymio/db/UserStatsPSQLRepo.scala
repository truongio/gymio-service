package com.gymio.db

import java.time.Instant
import java.time.Instant.now
import java.util.UUID

import com.gymio.db.UserStatsRecord.{toRecord, toUserStats}
import com.gymio.db.common.DatabaseProfile.api._
import com.gymio.db.common.DatabaseProfileProvider
import com.gymio.domain.infrastructure.UserStatsRepo
import com.gymio.domain.model.UserStats
import io.circe.Json
import io.circe.syntax._
import io.strongtyped.active.slick.Lens._
import io.strongtyped.active.slick.{ActiveRecord, EntityActions, Lens}
import slick.ast.BaseTypedType
import slick.lifted.{Rep, TableQuery, Tag}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UserStatsActions extends EntityActions with DatabaseProfileProvider {

  override val baseTypedType = implicitly[BaseTypedType[Id]]

  override type Entity      = UserStatsRecord
  override type Id          = UUID
  override type EntityTable = UserStatsTable

  override def tableQuery: TableQuery[EntityTable] = TableQuery[UserStatsTable]

  override def $id(table: EntityTable): Rep[Id] = table.id

  override def idLens: Lens[Entity, Option[Id]] = {
    lens { r: UserStatsRecord => Option(r.id)
    } { (r, id) => r.copy(id = id.get)
    }
  }
}

class UserStatsPSQLRepo(db: Database) extends UserStatsRepo {

  implicit class EntryExtensions(val model: UserStatsRecord)
      extends ActiveRecord(UserStatsActions)

  import UserStatsActions._

  def save(userId: UUID, us: UserStats): Future[UserStats] = {
    db.run(findOptionById(userId))
      .flatMap {
        case Some(_) => db.run(update(toRecord(userId, us)))
        case None    => db.run(insert(toRecord(userId, us)))
      }
      .map(_ => us)
  }

  def find(userId: UUID): Future[Seq[UserStats]] = {
    val tableQuery = TableQuery[UserStatsTable]
    val q          = tableQuery.filter(_.id === userId).result
    val result     = db.run(q)

    result.map(_.flatMap(toUserStats))
  }
}

class UserStatsTable(tag: Tag) extends Table[UserStatsRecord](tag, "user_stats") {

  def id: Rep[UUID]           = column("id", O.PrimaryKey)
  def data: Rep[Json]         = column("data")
  def timestamp: Rep[Instant] = column("timestamp")

  override def * =
    (id, data, timestamp) <> ((UserStatsRecord.apply _).tupled, UserStatsRecord.unapply)
}

case class UserStatsRecord(id: UUID, data: Json, timestamp: Instant)

object UserStatsRecord {
  def toRecord(userId: UUID, w: UserStats): UserStatsRecord = {
    UserStatsRecord(userId, w.asJson, now)
  }

  def toUserStats(w: UserStatsRecord): Seq[UserStats] = {
    w.data.as[UserStats].toSeq
  }
}

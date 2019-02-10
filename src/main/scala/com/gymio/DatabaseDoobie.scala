package com.gymio
import doobie._
import doobie.implicits._
import cats._
import cats.data._
import cats.effect.IO
import cats.implicits._
import scala.concurrent.ExecutionContext

object DatabaseDoobie {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:gymio",
    "postgres",
    ""
  )
}

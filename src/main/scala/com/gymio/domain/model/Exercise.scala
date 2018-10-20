package com.gymio.domain.model
import enumeratum.EnumEntry.UpperSnakecase
import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.auto._

import scala.collection.immutable

sealed trait Exercise extends EnumEntry

object Exercise extends Enum[Exercise] with CirceEnum[Exercise] {
  val values: immutable.IndexedSeq[Exercise] = findValues

  case object BenchPress    extends Exercise with UpperSnakecase
  case object Squat         extends Exercise with UpperSnakecase
  case object OverheadPress extends Exercise with UpperSnakecase
  case object Deadlift      extends Exercise with UpperSnakecase
}

package com.gymio.domain.model
import enumeratum.EnumEntry.UpperSnakecase
import enumeratum.{CirceEnum, Enum, EnumEntry}

import scala.collection.immutable

sealed trait Status extends EnumEntry

object Status extends Enum[Status] with CirceEnum[Status] {
  val values: immutable.IndexedSeq[Status] = findValues

  case object Active    extends Status with UpperSnakecase
  case object Completed extends Status with UpperSnakecase
}

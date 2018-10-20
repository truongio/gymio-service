package com.gymio.domain.model
import enumeratum.EnumEntry.Lowercase
import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.generic.auto._

import scala.collection.immutable
sealed trait WeightUnit extends EnumEntry

object WeightUnit extends Enum[WeightUnit] with CirceEnum[WeightUnit] {
  val values: immutable.IndexedSeq[WeightUnit] = findValues

  case object Kg  extends WeightUnit with Lowercase
  case object Lbs extends WeightUnit with Lowercase
}

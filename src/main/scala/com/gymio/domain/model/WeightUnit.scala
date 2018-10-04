package com.gymio.domain.model
import io.circe.generic.auto._

sealed trait WeightUnit

case object Lbs extends WeightUnit
case object Kg extends WeightUnit
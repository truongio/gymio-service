package com.gymio.domain.model

import io.circe.generic.auto._

case class Weight(value: Double, unit: WeightUnit)
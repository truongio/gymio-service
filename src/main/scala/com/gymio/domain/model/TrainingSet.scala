package com.gymio.domain.model

import io.circe.generic.auto._

case class TrainingSet(reps: Int, weightPercentage: Double)

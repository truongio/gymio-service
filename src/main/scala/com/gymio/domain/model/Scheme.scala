package com.gymio.domain.model
import io.circe.generic.auto._

case class Scheme(weekNo: Int, sets: Seq[TrainingSet])

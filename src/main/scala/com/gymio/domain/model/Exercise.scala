package com.gymio.domain.model

import io.circe.generic.auto._

trait Exercise

case object BenchPress
case object Deadlift
case object Squat
case object OverheadPress
package com.gymio.domain.model

import java.util.UUID
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}

case class Workout(
    workoutId: Int,
    userId: UUID,
    day: Int,
    week: Int,
    completedExercises: Seq[Event]
)

object Workout {
  implicit val config: Configuration = Configuration.default
  implicit val decoder: Decoder[Workout] = semiauto.deriveDecoder
}

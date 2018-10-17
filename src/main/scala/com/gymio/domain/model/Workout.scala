package com.gymio.domain.model

import java.util.UUID
import java.util.UUID.randomUUID
import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}

case class Workout(
    workoutId: UUID = randomUUID,
    userId: UUID = randomUUID,
    day: Int = 1,
    week: Int = 1,
    completedExercises: Seq[Event] = List()
)

object Workout {
  implicit val config: Configuration = Configuration.default
  implicit val decoder: Decoder[Workout] = semiauto.deriveDecoder
}

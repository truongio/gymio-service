package com.gymio.domain.model

import java.util.UUID
import java.util.UUID.randomUUID
import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}
import io.circe.{Decoder, Encoder}

case class Workout(
    id: UUID = randomUUID,
    userId: UUID = randomUUID,
    status: Status = Status.Active,
    day: Int = 1,
    week: Int = 1,
    completedExercises: Seq[Event] = List()
)

object Workout {
  implicit val c: Configuration    = Configuration.default
  implicit val d: Decoder[Workout] = semiauto.deriveDecoder
  implicit val e: Encoder[Workout] = semiauto.deriveEncoder
}

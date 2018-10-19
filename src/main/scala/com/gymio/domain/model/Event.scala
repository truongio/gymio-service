package com.gymio.domain.model

import io.circe.generic.auto._
import io.circe.generic.extras.Configuration.default
import io.circe.generic.extras.{Configuration, semiauto}
import io.circe.{Decoder, Encoder}

sealed trait Event

object Event {
  implicit val c: Configuration = default.withDiscriminator("eventType")
  implicit val d: Decoder[Event] = semiauto.deriveDecoder
  implicit val e: Encoder[Event] = semiauto.deriveEncoder
}

case class BenchCompleted(
    reps: Int,
    weight: Weight,
) extends Event

case class SquatCompleted(
    reps: Int,
    weight: Weight,
) extends Event

case class DeadliftCompleted(
    reps: Int,
    weight: Weight,
) extends Event

case class OverheadPressCompleted(
    reps: Int,
    weight: Weight,
) extends Event

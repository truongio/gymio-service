package com.gymio.domain.model

import cats.effect.IO
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import io.circe.generic.auto._

sealed trait Event

object Event {
  implicit val decoder: EntityDecoder[IO, Event] = jsonOf[IO, Event]
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

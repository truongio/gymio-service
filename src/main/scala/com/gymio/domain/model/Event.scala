package com.gymio.domain.model

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

sealed trait Event

case class BenchSetCompleted(reps: Int, weight: Weight) extends Event

case class SquatSetCompleted(reps: Int, weight: Weight) extends Event

case class DeadliftSetCompleted(reps: Int, weight: Weight) extends Event

case class PressSetCompleted(reps: Int, weight: Weight) extends Event

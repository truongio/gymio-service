package com.gymio.domain.model

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.generic.extras.Configuration.default
import io.circe.generic.extras.{Configuration, semiauto}

sealed trait Command

object Command {
  implicit val c: Configuration = default.withDiscriminator("eventType")
  implicit val d: Decoder[Command] = semiauto.deriveDecoder
}

case class CompleteExercise(exercise: Exercise, reps: Int, weight: Weight) extends Command
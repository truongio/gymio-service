package com.gymio.domain.model

import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}
import io.circe.{Decoder, Encoder}

sealed trait Command

object Command {
  implicit val config: Configuration = Configuration.default.withDiscriminator("eventType")
  implicit val decoder: Decoder[Command] = semiauto.deriveDecoder
  implicit val encoder: Encoder[Command] = semiauto.deriveEncoder
}

case class CompleteBenchPress(reps: Int, weight: Weight)    extends Command
case class CompleteSquat(reps: Int, weight: Weight)         extends Command
case class CompleteDeadlift(reps: Int, weight: Weight)      extends Command
case class CompleteOverheadPress(reps: Int, weight: Weight) extends Command

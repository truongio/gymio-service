package com.gymio.domain.model

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

sealed trait Command

object Command {
  implicit val decoder: EntityDecoder[IO, Command] = jsonOf[IO, Command]
}

case class CompleteBenchPress(reps: Int, weight: Weight)    extends Command
case class CompleteSquat(reps: Int, weight: Weight)         extends Command
case class CompleteDeadlift(reps: Int, weight: Weight)      extends Command
case class CompleteOverheadPress(reps: Int, weight: Weight) extends Command

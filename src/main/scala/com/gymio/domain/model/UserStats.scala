package com.gymio.domain.model

import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}
import io.circe.{Decoder, Encoder}

case class UserStats(trainingMaxes: Map[String, Weight], bodyWeight: Weight)

object UserStats {
  implicit val config: Configuration = Configuration.default
  implicit val decoder: Decoder[UserStats] = semiauto.deriveDecoder
  implicit val encoder: Encoder[UserStats] = semiauto.deriveEncoder
}

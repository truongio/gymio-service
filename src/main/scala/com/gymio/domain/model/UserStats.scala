package com.gymio.domain.model

import io.circe.generic.auto._
import io.circe.generic.extras.{Configuration, semiauto}
import io.circe.{Decoder, Encoder}

case class UserStats(trainingMaxes: Map[String, Weight], bodyWeight: Weight)

object UserStats {
  implicit val c: Configuration      = Configuration.default
  implicit val d: Decoder[UserStats] = semiauto.deriveDecoder
  implicit val e: Encoder[UserStats] = semiauto.deriveEncoder
}

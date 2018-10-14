package com.gymio.domain.model

import java.util.UUID

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class Session(userId: UUID, day: Int, week: Int, step: (Int, Int))

object Session {
  implicit val decoder: EntityDecoder[IO, Session] = jsonOf[IO, Session]
}

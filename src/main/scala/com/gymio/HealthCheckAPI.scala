package com.gymio
import cats.effect._
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._

object HealthCheckAPI {
  val healthCheckAPI: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(Json.obj("health" -> "OK".asJson))
  }
}

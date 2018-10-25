package com.gymio

import cats.effect._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object HealthCheckAPI {
  val healthCheckAPI: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("""{ "health": "OK" }""")
  }
}

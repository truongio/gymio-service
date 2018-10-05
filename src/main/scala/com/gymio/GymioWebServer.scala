package com.gymio

import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.server.blaze.BlazeServerBuilder

object GymioWebServer extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val service = new GymioService().gymioService

    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}

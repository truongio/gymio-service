package com.gymio.domain.infrastructure
import java.util.UUID

import com.gymio.domain.model.Workout
import io.circe.Decoder.Result

import scala.concurrent.Future

trait WorkoutRepo {
  def save(w: Workout): Future[Workout]

  def find(id: UUID): Future[Seq[Result[Workout]]]
}



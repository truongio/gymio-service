package com.gymio.domain.infrastructure
import java.util.UUID

import com.gymio.domain.model.Workout

import scala.concurrent.Future

trait WorkoutRepo {
  def save(userId: UUID, w: Workout): Future[Workout]

  def find(userId: UUID): Future[Seq[Workout]]
}



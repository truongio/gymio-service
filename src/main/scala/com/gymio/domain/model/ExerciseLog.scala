package com.gymio.domain.model

import java.util.UUID
import io.circe.generic.auto._

case class ExerciseLog(userId: UUID, exercises: Seq[Event])

package com.gymio.domain.model

import io.circe.generic.auto._

case class ExerciseLog(exercises: Seq[Event])

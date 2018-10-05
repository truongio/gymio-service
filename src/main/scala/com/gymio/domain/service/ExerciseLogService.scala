package com.gymio.domain.service

import com.gymio.domain.model.{Event, ExerciseLog}


object ExerciseLogService {
  def logExercise(event: Event)(log: ExerciseLog): ExerciseLog = {
    log.copy(exercises = log.exercises :+ event)
  }
}

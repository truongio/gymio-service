package com.gymio.domain.service

import com.gymio.domain.model.{Event, ExerciseLog}


object ExerciseLogService {
  def logCompletedExercise(event: Event)(log: ExerciseLog): ExerciseLog = {
    log.copy(exercises = log.exercises :+ event)
  }
}

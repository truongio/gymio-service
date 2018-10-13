package com.gymio.domain.service

import com.gymio.domain.model._

object ExerciseLogService {
  def decide(cmd: Command)(log: ExerciseLog): Either[Throwable, Event] = {
    cmd match {
      case CompleteBenchPress(reps, weight)    => Right(BenchCompleted(reps, weight))
      case CompleteSquat(reps, weight)         => Right(SquatCompleted(reps, weight))
      case CompleteDeadlift(reps, weight)      => Right(DeadliftCompleted(reps, weight))
      case CompleteOverheadPress(reps, weight) => Right(OverheadPressCompleted(reps, weight))
    }
  }

  def applyEvent(evt: Event)(log: ExerciseLog): ExerciseLog = {
    val oldExercises = log.exercises
    log.copy(exercises = oldExercises :+ evt)
  }

  def replay(events: List[Event])(initialLog: ExerciseLog): ExerciseLog = {
    events.foldLeft(initialLog)((log, e) => applyEvent(e)(log))
  }
}

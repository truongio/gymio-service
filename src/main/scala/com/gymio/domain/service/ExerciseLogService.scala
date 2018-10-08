package com.gymio.domain.service

import com.gymio.domain.model._

object ExerciseLogService {
  def decide(cmd: Command)(log: ExerciseLog): Either[Throwable, Event] = {
    cmd match {
      case CompleteBench(reps, weight)         => Right(BenchCompleted(reps, weight))
      case CompleteSquat(reps, weight)         => Right(SquatCompleted(reps, weight))
      case CompleteDeadlift(reps, weight)      => Right(DeadliftCompleted(reps, weight))
      case CompleteOverheadPress(reps, weight) => Right(OverheadPressCompleted(reps, weight))
      case _                                   => Left(new IllegalArgumentException())
    }
  }

  def applyEvent(evt: Event)(log: ExerciseLog): ExerciseLog = {
    log.copy(exercises = log.exercises :+ evt)
  }

  def replay(events: List[Event])(initialLog: ExerciseLog): ExerciseLog = {
    events.foldLeft(initialLog)((log, e) => applyEvent(e)(log))
  }
}

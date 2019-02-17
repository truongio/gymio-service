package com.gymio.domain.service

import com.gymio.domain.model.Exercise._
import com.gymio.domain.model._

object WorkoutService {
  type DayNr = Int
  type WeekNr = Int

  val ExercisesByDay: Map[DayNr, Seq[Exercise]] =
    Map(
      1 -> List(Squat, BenchPress),
      2 -> List(Deadlift, OverheadPress),
      3 -> List(BenchPress, Squat)
    )

  val SchemesByWeek: Map[WeekNr, Map[Int, Scheme]] =
    Map(
      1 -> Map(1 -> Scheme(5, 0.65), 2 -> Scheme(5, 0.75), 3 -> Scheme(5, 0.85)),
      2 -> Map(1 -> Scheme(3, 0.70), 2 -> Scheme(3, 0.80), 3 -> Scheme(3, 0.90)),
      3 -> Map(1 -> Scheme(5, 0.75), 2 -> Scheme(3, 0.85), 3 -> Scheme(1, 0.95))
    )

  def progress(w: Workout, exercises: Seq[Exercise]): Seq[(Exercise, Int)] = {
    val r = w
      .completedExercises
      .collect { case e: ExerciseCompleted => e }
      .groupBy(_.exercise)

    exercises.map(e => e -> r.getOrElse(e, Seq()).size)
  }

  def weightToLift(exercise: Exercise, stats: UserStats)(w: Workout): Option[Weight] = {
    for {
      schemes     <- SchemesByWeek.get(w.week)
      exercises   <- ExercisesByDay.get(w.day)
      progress    <- progress(w, exercises).find(_._2 < 3)
      scheme      <- schemes.get(progress._2)
      trainingMax <- stats.trainingMaxes.get(exercise.entryName)
    } yield Weight(trainingMax.value * scheme.weightPercentage, trainingMax.unit)
  }

  def nextWorkout(w: Workout): Workout = {
    val nextDay = (w.day % 3) + 1
    val nextWeek = if (w.day == 3) (w.week % 3) + 1 else w.week

    Workout(day = nextDay, week = nextWeek)
  }

  def decide(cmd: Command): Either[Throwable, Event] = cmd match {
    case c: CompleteExercise =>
      Right(ExerciseCompleted(c.exercise, c.reps, c.weight))
  }

  def apply(e: Event)(w: Workout): Workout = {
    val oldExercises = w.completedExercises
    w.copy(completedExercises = oldExercises :+ e)
  }

  def replay(events: List[Event])(initWorkout: Workout): Workout = {
    events.foldLeft(initWorkout)((w, e) => apply(e)(w))
  }
}

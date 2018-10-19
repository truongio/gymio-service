package com.gymio.domain.service

import com.gymio.domain.model._

object WorkoutService {
  type DayNr  = Int
  type WeekNr = Int

  val ExercisesByDay: Map[DayNr, Seq[Exercise]] =
    Map(
      1 -> List(Squat, BenchPress),
      2 -> List(Deadlift, OverheadPress),
      3 -> List(BenchPress, Squat)
    )

  val SchemesByWeek: Map[WeekNr, Map[Int, Scheme]] =
    Map(
      1 -> Map(1 -> Scheme(5, 0.65),
               2 -> Scheme(5, 0.75),
               3 -> Scheme(5, 0.85)),
      2 -> Map(1 -> Scheme(3, 0.70),
               2 -> Scheme(3, 0.80),
               3 -> Scheme(3, 0.90)),
      3 -> Map(1 -> Scheme(5, 0.75),
               2 -> Scheme(3, 0.85),
               3 -> Scheme(1, 0.95))
    )

  def progressOnFirstIncompleteExercise(w: Workout, exercises: Seq[Exercise]): Option[Int] = {
    val mappedExercises = exercises.map {
      case Squat         => "SquatCompleted"
      case BenchPress    => "BenchPressCompleted"
      case Deadlift      => "DeadliftCompleted"
      case OverheadPress => "OverheadPressCompleted"
    }

    val r = w.completedExercises
      .groupBy(e => e)
      .map(a => a._1.getClass.getSimpleName -> a._2)

    mappedExercises
      .map(e => e -> r.getOrElse(e, List()))
      .find(f => f._2.size < 3)
      .map(e => e._2.size)
  }

  def weightToLift(exercise: Exercise, stats: UserStats)(w: Workout): Option[Weight] = {
    for {
      schemes   <- SchemesByWeek.get(w.week)
      exercises <- ExercisesByDay.get(w.day)
      progress  <- progressOnFirstIncompleteExercise(w, exercises)
      scheme    <- schemes.get(progress)
      tM        <- stats.trainingMaxes.get(exercise)
      weight = Weight(tM.value * scheme.weightPercentage, tM.unit)
    } yield weight
  }

  def nextWorkout(w: Workout): Workout = {
    if (w.week == 3) {
      w.copy(week = 1, day = 1, completedExercises = List())
    } else if (w.day == 3) {
      w.copy(week = w.week + 1, day = 1, completedExercises = List())
    } else {
      w.copy(day = w.day + 1, completedExercises = List())
    }
  }

  def decide(command: Command): Either[Throwable, Event] = {
    command match {
      case CompleteBenchPress(reps, weight)    => Right(BenchCompleted(reps, weight))
      case CompleteSquat(reps, weight)         => Right(SquatCompleted(reps, weight))
      case CompleteDeadlift(reps, weight)      => Right(DeadliftCompleted(reps, weight))
      case CompleteOverheadPress(reps, weight) => Right(OverheadPressCompleted(reps, weight))
    }
  }

  def apply(e: Event)(w: Workout): Workout = {
    val oldExercises = w.completedExercises
    w.copy(completedExercises = oldExercises :+ e)
  }

  def replay(events: List[Event])(initWorkout: Workout): Workout = {
    events.foldLeft(initWorkout)((w, e) => apply(e)(w))
  }
}

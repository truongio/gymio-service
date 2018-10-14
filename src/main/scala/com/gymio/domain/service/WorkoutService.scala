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

  val SchemesByWeek: Map[WeekNr, Map[SchemeSet, Scheme]] =
    Map(
      1 -> Map(Set1 -> Scheme(5, 0.65),
               Set2 -> Scheme(5, 0.75),
               Set3 -> Scheme(5, 0.85)),
      2 -> Map(Set1 -> Scheme(3, 0.70),
               Set2 -> Scheme(3, 0.80),
               Set3 -> Scheme(3, 0.90)),
      3 -> Map(Set1 -> Scheme(5, 0.75),
               Set2 -> Scheme(3, 0.85),
               Set3 -> Scheme(1, 0.95))
    )

  def calculateWeightForScheme(stats: UserStats)(w: Workout): Option[Weight] = {
    for {
      schemes     <- SchemesByWeek.get(w.week)
      scheme      <- schemes.get(w.step._1)
      exercises   <- ExercisesByDay.get(w.day)
      exercise    <- exercises.find(_ == w.step._2)
      trainingMax <- stats.trainingMaxes.get(exercise)
      weight      = Weight(trainingMax.value * scheme.weightPercentage, trainingMax.unit)
    } yield weight
  }

  def getNextWorkout(workout: Workout): Workout = {
    if (workout.week == 3) {
      workout.copy(week = 1, day = 1)
    } else if (workout.day == 3) {
      workout.copy(week = workout.week + 1, day = 1)
    } else {
      workout.copy(day = workout.day + 1)
    }
  }
}

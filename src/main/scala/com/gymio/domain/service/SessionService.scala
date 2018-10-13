package com.gymio.domain.service

import com.gymio.domain.model._

object SessionService {
  type DayNr  = Int
  type WeekNr = Int
  type Step   = Int

  val ExercisesByDay: Map[DayNr, Seq[Exercise]] =
    Map(
      1 -> List(Squat, BenchPress),
      2 -> List(Deadlift, OverheadPress),
      3 -> List(BenchPress, Squat)
  )

  val SchemesByWeek: Map[WeekNr, Map[Step, Scheme]] =
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

  def calculateWeightForScheme(stats: UserStats)(s: Session): Option[Double] = {
    for {
      schemes   <- SchemesByWeek.get(s.week)
      scheme    <- schemes.get(s.step._1)
      exercises <- ExercisesByDay.get(s.day)
      exercise  <- exercises.lift(s.step._2)
      res       <- stats.trainingMaxes.get(exercise).map(w => w.value * scheme.weightPercentage)
    } yield res
  }
}

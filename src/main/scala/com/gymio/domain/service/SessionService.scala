package com.gymio.domain.service

import com.gymio.domain.model._

object SessionService {
  type DayNr  = Int
  type WeekNr = Int
  type Step   = Int

  val exercisesByDay: Map[DayNr, Seq[Exercise]] =
    Map(
      1 -> List(Squat, BenchPress),
      2 -> List(Deadlift, OverheadPress),
      3 -> List(BenchPress, Squat)
    )

  val setSchemesByWeek: Map[WeekNr, Map[Step, SetScheme]] =
    Map(
      1 -> Map(1 -> SetScheme(5, 0.65),
               2 -> SetScheme(5, 0.75),
               3 -> SetScheme(5, 0.85)),
      2 -> Map(1 -> SetScheme(3, 0.70),
               2 -> SetScheme(3, 0.80),
               3 -> SetScheme(3, 0.90)),
      3 -> Map(1 -> SetScheme(5, 0.75),
               2 -> SetScheme(3, 0.85),
               3 -> SetScheme(1, 0.95))
    )

  def decideExercises(dayNr: DayNr): Option[Seq[Exercise]] = {
    exercisesByDay.get(dayNr)
  }

  def decideSetSchemes(weekNr: WeekNr): Option[Map[Step, SetScheme]] = {
    setSchemesByWeek.get(weekNr)
  }
}

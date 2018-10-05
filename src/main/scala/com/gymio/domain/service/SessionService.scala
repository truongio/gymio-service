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

  def calculateWeightForScheme(stats: UserStats)(s: Session): Option[Double] = {
    for {
      schemes  <- setSchemesByWeek.get(s.week)
      scheme   <- schemes.get(s.step._1)
      exercise <- exercisesByDay.get(s.day).map(exs => exs.apply(s.step._2))
      res <- stats.trainingMaxes
        .get(exercise)
        .map(w => w.value * scheme.weightPercentage)
    } yield res

    /* Alternative way to do it below. The above thing is called "for comprehension"
     * and is basically just a series of flatMaps. flatMap is a combination of
     * .map and .flatten. .map is a higher order function that operates on all
     * values in the monad rather than the monad itself.
     * For example, List is a monad. So for a List[Int], .map will operate
     * on the values that are of type Int.
     * List(1, 2, 3, 4, 5).map(intValue => intValue * 2) will yield:
     * List(2, 4, 6, 8, 10) where intValue => intValue * 2 is just
     * an anonymous function that takes an intValue and returns the product
     */

//    setSchemesByWeek.get(s.week)
//      .flatMap(schemes => schemes.get(s.step._1)
//        .flatMap(scheme => exercisesByDay.get(s.day).map(exs => exs.apply(s.step._2))
//          .flatMap(exercise => stats.trainingMaxes.get(exercise).map(w => w.value * scheme.weightPercentage))))
  }
}

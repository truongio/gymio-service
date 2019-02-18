package com.gymio.domain.service

import com.gymio.domain.model.Exercise._
import com.gymio.domain.model._

object WorkoutService {

  val ExercisesByDay: Seq[Seq[Exercise]] =
    Seq(
      Seq(Squat, BenchPress),
      Seq(Deadlift, OverheadPress),
      Seq(BenchPress, Squat)
    )

  val SchemesByWeek: Seq[Scheme] =
    Seq(
      Scheme(
        weekNo = 1,
        sets = Seq(
          TrainingSet(reps = 5, weightPercentage = 0.65),
          TrainingSet(reps = 5, weightPercentage = 0.75),
          TrainingSet(reps = 5, weightPercentage = 0.85)
        )
      ),
      Scheme(
        weekNo = 2,
        sets = Seq(
          TrainingSet(reps = 3, weightPercentage = 0.70),
          TrainingSet(reps = 3, weightPercentage = 0.80),
          TrainingSet(reps = 3, weightPercentage = 0.90)
        )
      ),
      Scheme(
        weekNo = 3,
        sets = Seq(
          TrainingSet(reps = 5, weightPercentage = 0.75),
          TrainingSet(reps = 3, weightPercentage = 0.85),
          TrainingSet(reps = 1, weightPercentage = 0.95)
        )
      )
    )

  def incompleteExerciseProgress(
    completedExercises: Seq[Event],
    exercises: Seq[Exercise]
  ): Option[(Exercise, Int)] = {
    completedExercises
      .collect { case e: ExerciseCompleted => e }
      .groupBy(identity)
      .find(_._2.size < 3)
      .map(p => p._1.exercise -> p._2.size)
  }

  def weightToLift(exercise: Exercise, stats: UserStats)(w: Workout): Option[Weight] = {
    for {
      scheme      <- SchemesByWeek.find(_.weekNo == w.week)
      exercises   <- ExercisesByDay.lift(w.day + 1)
      progress    <- incompleteExerciseProgress(w.completedExercises, exercises)
      set         <- scheme.sets.lift(progress._2 + 1)
      trainingMax <- stats.trainingMaxes.get(exercise.entryName)
    } yield Weight(trainingMax.value * set.weightPercentage, trainingMax.unit)
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

  def replay(events: Seq[Event])(initWorkout: Workout): Workout = {
    events.foldLeft(initWorkout)((w, e) => apply(e)(w))
  }
}

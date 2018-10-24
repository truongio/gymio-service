package com.gymio.domain.service
import com.gymio.domain.model.Workout
import com.gymio.domain.service.WorkoutService.nextWorkout
import org.scalatest.{FlatSpec, Matchers}

class WorkoutServiceTest extends FlatSpec with Matchers {
  it should "become Day 2 and Week 1 on next workout" in {
    val w = Workout()

    nextWorkout(w).day should be(2)
    nextWorkout(w).week should be(1)
  }

  it should "become Day 1 and Week 2 on next workout" in {
    val w = Workout(day = 3)

    nextWorkout(w).day should be(1)
    nextWorkout(w).week should be(2)

  }

  it should "become Day 3 and Week 1 on next workout" in {
    val w = Workout(day = 2)

    nextWorkout(w).day should be(3)
    nextWorkout(w).week should be(1)

  }

  it should "become Day 1 and Week 1 on next workout" in {
    val w = Workout(day = 2)

    nextWorkout(w).day should be(3)
    nextWorkout(w).week should be(1)
  }

}

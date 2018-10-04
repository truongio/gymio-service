package domain.model

trait Event
case class BenchSetCompleted(reps: Int, weight: Weight) extends Event
case class SquatSetCompleted(reps: Int, weight: Weight) extends Event
case class DeadliftSetCompleted(reps: Int, weight: Weight) extends Event
case class PressSetCompleted(reps: Int, weight: Weight) extends Event

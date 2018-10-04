package domain.model

import java.util.UUID

case class ExerciseLog(userId: UUID, exercises: Seq[Event])

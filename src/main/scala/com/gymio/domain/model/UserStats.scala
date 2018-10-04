package domain.model

case class UserStats(trainingMaxes: Map[Exercise, Weight],
                     bodyWeight: Weight)

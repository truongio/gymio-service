package com.gymio.domain.service

import com.gymio.domain.model.{Exercise, UserStats, Weight}


object UserStatsService {
  def calculateTrainingMax(realMax: Weight): Weight = {
    Weight(realMax.value * 0.9, realMax.unit)
  }

  def updateTrainingMax(exercise: Exercise, weight: Weight)(userStats: UserStats): UserStats = {
    val oldMaxes = userStats.trainingMaxes
    userStats.copy(trainingMaxes = oldMaxes + (exercise -> weight))
  }
}

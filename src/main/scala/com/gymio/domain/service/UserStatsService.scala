package com.gymio.domain.service

import com.gymio.domain.model.{Exercise, UserStats, Weight}

object UserStatsService {
  def trainingMax(realMax: Weight): Weight = {
    Weight(realMax.value * 0.9, realMax.unit)
  }

  def updateTrainingMax(exs: Exercise, w: Weight)(stats: UserStats): UserStats = {
    val oldMaxes = stats.trainingMaxes
    val newMaxes = oldMaxes + (exs.entryName -> w)
    stats.copy(trainingMaxes = newMaxes)
  }
}

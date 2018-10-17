package com.gymio.domain.service

import com.gymio.domain.model.{Exercise, UserStats, Weight}

object UserStatsService {
  def calculateTM(realMax: Weight): Weight = {
    Weight(realMax.value * 0.9, realMax.unit)
  }

  def updateTM(exs: Exercise, w: Weight)(stats: UserStats): UserStats = {
    val oldMaxes = stats.trainingMaxes
    val newMaxes = oldMaxes + (exs -> w)
    stats.copy(trainingMaxes = newMaxes)
  }
}



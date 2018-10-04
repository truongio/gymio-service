package com.gymio.domain.model

import io.circe.generic.auto._

case class UserStats(trainingMaxes: Map[Exercise, Weight],
                     bodyWeight: Weight)

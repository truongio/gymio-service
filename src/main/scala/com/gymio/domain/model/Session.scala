package com.gymio.domain.model

import io.circe.generic.auto._

case class Session(day: Int, week: Int, step: (Int, Int))

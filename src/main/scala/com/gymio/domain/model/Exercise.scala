package com.gymio.domain.model

sealed trait Exercise

case object BenchPress    extends Exercise
case object Deadlift      extends Exercise
case object Squat         extends Exercise
case object OverheadPress extends Exercise

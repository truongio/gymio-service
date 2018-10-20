package com.gymio.domain.infrastructure

import java.util.UUID

import com.gymio.domain.model.UserStats

import scala.concurrent.Future

trait UserStatsRepo {
  def save(userId: UUID, w: UserStats): Future[UserStats]

  def find(userId: UUID): Future[Seq[UserStats]]
}



package com.gymio.db.common

import com.github.tminglei.slickpg._

trait DatabaseProfile
    extends ExPostgresProfile
    with PgDate2Support
    with PgCirceJsonSupport {

  def pgjson = "jsonb"

  override val api: GymioAPI = GymioAPI

  trait GymioAPI extends API with JsonImplicits with DateTimeImplicits

  object GymioAPI extends GymioAPI

}

object DatabaseProfile extends DatabaseProfile

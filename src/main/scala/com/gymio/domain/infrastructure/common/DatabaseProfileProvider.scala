package com.gymio.domain.infrastructure.common

import io.strongtyped.active.slick.JdbcProfileProvider

trait DatabaseProfileProvider extends JdbcProfileProvider {

  override type JP = DatabaseProfile
  override val jdbcProfile: JP = DatabaseProfile

}

package com.gymio.domain.model

import java.time.Instant
import java.util.UUID
import io.circe.generic.auto._

case class EventWrapper(id: UUID, eventType: String, timestamp: Instant, data: Event)

package domain.model

import java.time.Instant
import java.util.UUID

case class EventWrapper(id: UUID, eventType: String, timestamp: Instant, data: Event)

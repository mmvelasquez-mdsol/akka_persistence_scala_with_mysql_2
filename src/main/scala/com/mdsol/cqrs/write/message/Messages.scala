package com.mdsol.cqrs.write.message

import java.util.UUID

// Metadata
case class Metadata(correlationId: String, traceId: String, userId: String, version: Int = 1) {
  def incrementVersion(): Metadata =
    copy(version = version + 1)
}

// - Command Messages
sealed class Command

case class CreateCtaCommand(metadata: Metadata, name: String) extends Command

case class UpdateCtaCommand(metadata: Metadata, id: UUID, name: String) extends Command

// - Event  Messages
sealed class DomainEvent

case class CtaCreated(metadata: Metadata, name: String) extends DomainEvent

case class CtaUpdated(metadata: Metadata, name: String) extends DomainEvent

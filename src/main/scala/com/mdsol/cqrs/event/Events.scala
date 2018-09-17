package com.mdsol.cqrs.event

// - Event  Messages
sealed class DomainEvent

case class CtaCreated(metadata: Metadata, name: String) extends DomainEvent

case class CtaUpdated(metadata: Metadata, name: String) extends DomainEvent

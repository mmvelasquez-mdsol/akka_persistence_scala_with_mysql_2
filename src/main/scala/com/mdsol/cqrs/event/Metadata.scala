package com.mdsol.cqrs.event

// Metadata
case class Metadata(correlationId: String, traceId: String, userId: String, version: Int = 1) {
  def incrementVersion(): Metadata =
    copy(version = version + 1)
}
package com.mdsol.cqrs.write.dto

case class CreateCtaRequestDto(userId: String, correlationId: String, traceId: String, name: String)

case class UpdateCtaRequestDto(
  userId: String,
  correlationId: String,
  traceId: String,
  version: Int,
  id: String,
  name: String
)

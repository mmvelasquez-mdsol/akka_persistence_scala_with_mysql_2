package com.mdsol.cqrs.write

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import com.mdsol.cqrs.write.dto.UpdateCtaRequestDto
import com.mdsol.cqrs.write.handler.CtaCommandHandler
import com.mdsol.cqrs.write.message._

// API Layer
object CtaUpdateWriteMain extends App {
  val system = ActorSystem("CtaActorSystem")

  val updateCtaRequestDto1 =
    UpdateCtaRequestDto(
      "userUuid2",
      "txUuid2",
      "traceUuid2",
      0, // Version for Stale state validation
      "18267806-4595-413b-b674-99e0b0142441", // Cta Id - Aggregate Root Id
      "Cta Name CCC"
    )
  updateCta(updateCtaRequestDto1)

  val updateCtaRequestDto2 =
    UpdateCtaRequestDto(
      "userUuid3",
      "txUuid3",
      "traceUuid3",
      0, // Version for Stale state validation
      "18267806-4595-413b-b674-99e0b0142441", // Cta Id - Aggregate Root Id
      "Cta Name DDD"
    )
  updateCta(updateCtaRequestDto2)

  println("All messages sent")
  Thread.sleep(5000)
  system.terminate()
  println("Actor System finished")

  def updateCta(updateCtaRequestDto: UpdateCtaRequestDto): Unit = {
    // Creating Command Handler
    val ctaCommandHandler = system
      .actorOf(Props[CtaCommandHandler], name = s"CommandHandler_${UUID.randomUUID().toString}")

    // Mapping Dto to Command
    val updateCtaCommand = UpdateCtaCommand(
      Metadata(updateCtaRequestDto.correlationId,
               updateCtaRequestDto.traceId,
               updateCtaRequestDto.userId,
               updateCtaRequestDto.version),
      UUID.fromString(updateCtaRequestDto.id),
      updateCtaRequestDto.name
    )

    // Send command to handler
    ctaCommandHandler ! updateCtaCommand
  }

}

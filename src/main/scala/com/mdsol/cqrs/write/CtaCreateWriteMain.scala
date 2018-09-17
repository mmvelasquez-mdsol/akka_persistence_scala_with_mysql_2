package com.mdsol.cqrs.write

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import com.mdsol.cqrs.write.dto.{CreateCtaRequestDto}
import com.mdsol.cqrs.write.handler.CtaCommandHandler
import com.mdsol.cqrs.write.message._

// API Layer
object CtaCreateWriteMain extends App {
  val system = ActorSystem("CtaActorSystem")

  val ctaCommandHandler =
    system.actorOf(Props[CtaCommandHandler], name = s"CommandHandler_${UUID.randomUUID().toString}")

  // Receiving Dto
  var createCtaRequestDto =
    CreateCtaRequestDto("userUuid1", "txUuid1", "traceUuid1", "Cta Name AAA")
  // Mapping Dto to Command
  var createCommand = CreateCtaCommand(Metadata(createCtaRequestDto.correlationId,
                                                createCtaRequestDto.traceId,
                                                createCtaRequestDto.userId),
                                       createCtaRequestDto.name)
  ctaCommandHandler ! createCommand

  println("All messages sent")
  Thread.sleep(2500)
  system.terminate()
  println("Actor System finished")
}

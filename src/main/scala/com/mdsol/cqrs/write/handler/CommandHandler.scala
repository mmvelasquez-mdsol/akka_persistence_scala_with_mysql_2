package com.mdsol.cqrs.write.handler

import java.util.UUID

import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import akka.actor.SupervisorStrategy.Stop
import com.mdsol.cqrs.write.aggregate.ClinicalTrialAgreement
import com.mdsol.cqrs.write.exception.{OptimisticLockingException, StaleStateException}
import com.mdsol.cqrs.write.message.{CreateCtaCommand, UpdateCtaCommand}

// Application Layer
class CtaCommandHandler extends Actor with ActorLogging {

  override def supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 0) {
    case _: OptimisticLockingException => handlerException("OptimisticLockingException")
    case _: StaleStateException => handlerException("StaleStateException")
    case _ => handlerException("unhandled exception")
  }

  override def receive: Receive = {
    case createCtaCommand: CreateCtaCommand => handleCreateCtaCommand(createCtaCommand)
    case updateCtaCommand: UpdateCtaCommand => handleUpdateCtaCommand(updateCtaCommand)
    case _ =>
  }

  private def handleCreateCtaCommand(createCtaCommand: CreateCtaCommand): Unit = {
    //  Here - Superficial validation
    val ctaGeneratedGuid = UUID.randomUUID()
    val ctaAggregateRoot = context
      .actorOf(Props(new ClinicalTrialAgreement(ctaGeneratedGuid)), ctaGeneratedGuid.toString)
    ctaAggregateRoot ! createCtaCommand
  }

  private def handleUpdateCtaCommand(updateCtaCommand: UpdateCtaCommand): Unit = {
    Thread.sleep(1000) // Forcing OptimisticLooking error
    val ctaAggregateRoot = context
      .actorOf(Props(new ClinicalTrialAgreement(updateCtaCommand.id)), updateCtaCommand.id.toString)
    ctaAggregateRoot ! updateCtaCommand
  }

  private def handlerException(exception: String) = {
    log.error(s"Supervisor detected $exception while performing Cta command")
    Stop
  }
}

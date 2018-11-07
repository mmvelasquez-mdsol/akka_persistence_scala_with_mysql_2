package com.mdsol.cqrs.write.aggregate

import java.sql.SQLException
import java.util.UUID

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import com.mdsol.cqrs.event.{CtaCreated, CtaUpdated, DomainEvent}
import com.mdsol.cqrs.write.exception.{OptimisticLockingException, StaleStateException}
import com.mdsol.cqrs.write.command._

// Aggregate Root - Domain Layer
class ClinicalTrialAgreement(guid: UUID) extends PersistentActor with ActorLogging {

  // Internal Current View
  case class CurrentState(version: Int = 1) {
    def updated(event: CtaUpdated): CurrentState =
      copy(version = event.metadata.version)

    def isVersionEqualTo(version: Int): Boolean = this.version == version
  }

  private var currentState: CurrentState = CurrentState()

  override def preStart(): Unit = log.info("CtaAggregateRoot actor alive!")

  override def postStop(): Unit = log.info("Goodbye CtaAggregateRoot actor!")

  override def persistenceId: String = guid.toString

  override def receiveRecover: Receive = {
    case event: CtaCreated => handleEvent(event)
    case event: CtaUpdated => handleEvent(event)
    case _ =>
  }

  override def receiveCommand: Receive = {
    case createCtaCommand: CreateCtaCommand => handleCreateCtaCommand(createCtaCommand)
    case updateCtaCommand: UpdateCtaCommand => handleUpdateCtaCommand(updateCtaCommand)
    case _ =>
  }

  private def handleCreateCtaCommand(createCtaCommand: CreateCtaCommand): Unit = {
    log.info(s"Receiving Command -> $createCtaCommand")
    val ctaCreated = CtaCreated(createCtaCommand.metadata, createCtaCommand.name)
    persist(ctaCreated) { event =>
      handleEvent(event)
    }
  }

  private def handleUpdateCtaCommand(updateCtaCommand: UpdateCtaCommand): Unit = {
    log.info(s"Receiving Command -> $updateCtaCommand")
    if (currentState.isVersionEqualTo(updateCtaCommand.metadata.version)) { // Verify stale state
      val updateEvent =
        CtaUpdated(updateCtaCommand.metadata.incrementVersion(), updateCtaCommand.name)
      persist(updateEvent) { event =>
        handleEvent(event)
      }
    } else {
      throw StaleStateException(updateCtaCommand.metadata) // It could be an event
    }
  }

  override protected def onPersistFailure(cause: Throwable, event: Any, seqNr: Long): Unit = {
    super.onPersistFailure(cause, event, seqNr)
    cause match {
      case dbException: SQLException if dbException.getMessage.contains("Duplicate entry") =>
        throw OptimisticLockingException(event.asInstanceOf[CtaUpdated].metadata) // It could be an event
      case _: Exception => throw cause
    }
  }

  private def handleEvent(event: DomainEvent): Unit = {
    log.info(s"Apply Event -> ${event.toString}")
    currentState = event match {
      case _: CtaCreated => CurrentState()
      case ctaUpdated: CtaUpdated => currentState.updated(ctaUpdated)
      case _ => currentState
    }
    log.info(s"-> Current State - ${currentState.toString} with SeqNr: $lastSequenceNr")
  }

}

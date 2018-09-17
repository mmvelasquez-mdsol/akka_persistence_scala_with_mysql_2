package com.mdsol.cqrs.read

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import com.mdsol.cqrs.event.{CtaCreated, CtaUpdated, DomainEvent}

object CtaReadEventsForPersistenceIdMain extends App {
  implicit val system: ActorSystem = ActorSystem("CtaReadAllPersistenceIds")
  implicit val mat: Materializer = ActorMaterializer()(system)

  val readJournal: JdbcReadJournal = PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)
  val willNotCompleteTheStream: Source[EventEnvelope, NotUsed] = readJournal.eventsByPersistenceId("638de6aa-ddec-4bff-82dc-24302a7a8c3e", 0, Long.MaxValue)

  println(">>> Getting ready to stream Events for Aggregate Root: " + "638de6aa-ddec-4bff-82dc-24302a7a8c3e")

  willNotCompleteTheStream.runForeach(eventEnvelope => {
    val persistenceId = eventEnvelope.persistenceId
    val sequenceNumber = eventEnvelope.sequenceNr
    println("Got Streamed an Event with Sequence : # " + sequenceNumber)
    handleEvent(eventEnvelope.event.asInstanceOf[DomainEvent])
  })


  private def handleEvent(event: DomainEvent): Unit = {
    event match {
      case ctaCreated: CtaCreated => println(" -> Received Event: " + ctaCreated)
      case ctaUpdated: CtaUpdated => println(" -> Received Event: " + ctaUpdated)
      case notRecognised => println(" -> Any other event ! " + notRecognised)
    }
  }

}

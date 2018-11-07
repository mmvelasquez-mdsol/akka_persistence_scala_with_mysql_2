package com.mdsol.cqrs.read

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}

object CtaReadEventsForEventTagMain extends App {
  implicit val system: ActorSystem = ActorSystem("CtaReadAllPersistenceIds")
  implicit val mat: Materializer = ActorMaterializer()(system)

  val readJournal: JdbcReadJournal = PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)
  val willNotCompleteTheStream: Source[EventEnvelope, NotUsed] = readJournal.currentEventsByTag("cta", 1)

  println(">>> Getting ready to stream Events tagged with: " + "cta")

  willNotCompleteTheStream.runForeach(eventEnvelope => {
    val persistenceId = eventEnvelope.persistenceId
    val sequenceNumber = eventEnvelope.sequenceNr
    val offset = eventEnvelope.offset
    println(s"Got Streamed an Event tagged for AR ID: $persistenceId with sequenceNumber: # " + sequenceNumber + " # offset: " + offset)
    println("\twith Event data : # " + eventEnvelope.event)
  })

}
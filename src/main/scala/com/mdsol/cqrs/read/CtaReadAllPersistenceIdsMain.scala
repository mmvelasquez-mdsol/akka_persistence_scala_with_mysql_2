package com.mdsol.cqrs.read

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}

object CtaReadAllPersistenceIdsMain extends App {
  implicit val system: ActorSystem = ActorSystem("CtaReadAllPersistenceIds")
  implicit val mat: Materializer = ActorMaterializer()(system)

val readJournal: JdbcReadJournal = PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)
val willNotCompleteTheStream: Source[String, NotUsed] = readJournal.persistenceIds()

willNotCompleteTheStream.runForeach(println)

}

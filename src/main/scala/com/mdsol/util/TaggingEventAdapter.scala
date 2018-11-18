package com.mdsol.util

import akka.persistence.journal.{EventAdapter, EventSeq, Tagged}
import com.mdsol.cqrs.event.DomainEvent

class TaggingEventAdapter extends EventAdapter {

  override def manifest(event: Any): String = ""

  override def toJournal(event: Any): Any = event match {
    case _: DomainEvent => Tagged(event, Set( "cta"))
    case _ => event
  }

  override def fromJournal(event: Any, manifest: String): EventSeq = {
    EventSeq.single(event)
  }

}
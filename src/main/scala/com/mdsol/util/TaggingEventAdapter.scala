package com.mdsol.util

import akka.persistence.journal.{Tagged, WriteEventAdapter}
import com.mdsol.cqrs.event.{CtaCreated, CtaUpdated}

class TaggingEventAdapter extends WriteEventAdapter {

  override def manifest(event: Any): String = ""

  override def toJournal(event: Any): Any = event match {
    case _: CtaCreated => Tagged(event, Set( "cta"))
    case _: CtaUpdated => Tagged(event, Set( "cta"))
    case _ => event
  }

}
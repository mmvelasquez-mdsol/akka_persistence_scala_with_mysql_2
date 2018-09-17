package com.mdsol.cqrs.write.exception

import com.mdsol.cqrs.event.Metadata

sealed class CtaException(name: String, metadata: Metadata) extends Exception(name)

case class OptimisticLockingException(metadata: Metadata)
    extends CtaException("OptimisticLocking", metadata)

case class StaleStateException(metadata: Metadata)
    extends CtaException("StaleStateException", metadata)

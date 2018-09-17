package com.mdsol.cqrs.write.command

import java.util.UUID
import com.mdsol.cqrs.event.Metadata

// - Command Messages
sealed class Command

case class CreateCtaCommand(metadata: Metadata, name: String) extends Command

case class UpdateCtaCommand(metadata: Metadata, id: UUID, name: String) extends Command


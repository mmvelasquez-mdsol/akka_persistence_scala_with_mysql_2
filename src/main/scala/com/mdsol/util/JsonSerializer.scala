package com.mdsol.util

import akka.serialization.Serializer
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, Formats}

case class EventWrapper(manifest: String, payload: String)

class JsonSerializer extends Serializer {

  implicit val formats: Formats = DefaultFormats

  override def identifier: Int = Int.MaxValue

  override def includeManifest: Boolean = true

  override def toBinary(o: AnyRef): Array[Byte] =
    write(EventWrapper(o.getClass.getName, write(o))).getBytes()

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    val wrapper: EventWrapper = parse(new String(bytes)).extract[EventWrapper]
    implicit val mf = Manifest.classType(Class.forName(wrapper.manifest))
    read(wrapper.payload)
  }
}

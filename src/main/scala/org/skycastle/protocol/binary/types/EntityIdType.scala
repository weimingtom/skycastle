package org.skycastle.protocol.binary.types

import _root_.org.skycastle.entity.EntityId
import java.nio.ByteBuffer

/**
 * 
 */

object EntityIdType extends SerializableType {
  val number : Byte = 11
  type T = EntityId
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = StringType.encode( buffer, value.id )

  def decode(buffer: ByteBuffer) = EntityId( StringType.decode( buffer ) )

  def length(value: T) = StringType.length( value.id )

}
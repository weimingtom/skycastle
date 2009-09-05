package org.skycastle.protocol.binary.types

import _root_.org.skycastle.entity.EntityId
import java.nio.ByteBuffer

/**
 * 
 */

object EntityIdType extends SerializableType {
  val number = 11
  val name = 'entityId
  type T = EntityId

  def encode(buffer: ByteBuffer, value: T) = StringType.encode( buffer, value.id )

  def decode(buffer: ByteBuffer) = EntityId( StringType.decode( buffer ) )

  def length(value: T) = StringType.length( value.id )

}
package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object ByteType extends SerializableType {

  val number : Byte = 2
  type T = Byte
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = buffer.put( value )

  def decode(buffer: ByteBuffer) = buffer.get

  def length(value: T) = 1
}
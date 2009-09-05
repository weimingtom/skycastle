package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object FloatType extends SerializableType {
  val number : Byte = 6
  type T = Float
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = buffer.putFloat( value )
  def decode(buffer: ByteBuffer) = buffer.getFloat
  def length(value: T) = 4

}
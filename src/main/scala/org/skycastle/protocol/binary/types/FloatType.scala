package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object FloatType extends SerializableType {
  type T = Float

  val number = 6
  val name = 'float

  def encode(buffer: ByteBuffer, value: T) = buffer.putFloat( value )
  def decode(buffer: ByteBuffer) = buffer.getFloat
  def length(value: T) = 4

}
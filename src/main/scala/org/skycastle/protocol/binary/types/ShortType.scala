package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object ShortType extends SerializableType {

  type T = Short
  val number : Byte = 3
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = buffer.putShort( value )
  def decode(buffer: ByteBuffer) = buffer.getShort
  def length(value: T) = 2
}
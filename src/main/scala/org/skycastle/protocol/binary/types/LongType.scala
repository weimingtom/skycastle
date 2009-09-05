package org.skycastle.protocol.binary

import java.nio.ByteBuffer

/**
 * 
 */

object LongType extends SerializableType {
  type T = Long
  val number : Byte= 5
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = buffer.putLong( value )
  def decode(buffer: ByteBuffer) = buffer.getLong
  def length(value: T) = 8

}
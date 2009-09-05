package org.skycastle.protocol.binary

import java.nio.ByteBuffer

/**
 * 
 */

object ShortType extends SerializableType {
  type T = Short

  val number = 3
  val name = 'short

  def encode(buffer: ByteBuffer, value: T) = buffer.putShort( value )
  def decode(buffer: ByteBuffer) = buffer.getShort
  def length(value: T) = 2
}
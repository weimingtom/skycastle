package org.skycastle.protocol.binary

import java.nio.ByteBuffer

/**
 * 
 */

object LongType extends SerializableType {
  type T = Long

  val number = 5
  val name = 'long

  def encode(buffer: ByteBuffer, value: T) = buffer.putLong( value )
  def decode(buffer: ByteBuffer) = buffer.getLong
  def length(value: T) = 8

}
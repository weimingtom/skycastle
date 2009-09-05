package org.skycastle.protocol.binary

import java.nio.ByteBuffer

/**
 * 
 */

object ByteType extends SerializableType {

  val number = 2
  val name = 'byte

  type T = Byte

  def encode(buffer: ByteBuffer, value: T) = buffer.put( value )

  def decode(buffer: ByteBuffer) = buffer.get

  def length(value: T) = 1
}
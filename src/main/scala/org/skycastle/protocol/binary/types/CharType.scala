package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object CharType extends SerializableType {
  type T = Char

  val number = 8
  val name = 'char

  def encode(buffer: ByteBuffer, value: T) = buffer.putChar( value )
  def decode(buffer: ByteBuffer) = buffer.getChar
  def length(value: T) = 2

}
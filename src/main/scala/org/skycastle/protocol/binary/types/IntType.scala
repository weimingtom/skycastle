package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object IntType extends SerializableType {

  val INT_LEN = 4

  type T = Int
  val number : Byte = 4
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) = buffer.putInt( value )
  def decode(buffer: ByteBuffer) = buffer.getInt
  def length(value: T) = INT_LEN

}
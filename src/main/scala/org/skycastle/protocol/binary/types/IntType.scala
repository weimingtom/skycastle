package org.skycastle.protocol.binary

import java.nio.ByteBuffer

/**
 * 
 */

object IntType extends SerializableType {
  type T = Int

  val INT_LEN = 4
  
  val number = 4
  val name = 'int

  def encode(buffer: ByteBuffer, value: T) = buffer.putInt( value )
  def decode(buffer: ByteBuffer) = buffer.getInt
  def length(value: T) = INT_LEN

}
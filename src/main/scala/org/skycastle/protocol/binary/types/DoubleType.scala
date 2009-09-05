package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object DoubleType extends SerializableType {
  type T = Double
  val number : Byte = 7
  val kind = classOf[T]


  def encode(buffer: ByteBuffer, value: T) = buffer.putDouble( value )
  def decode(buffer: ByteBuffer) = buffer.getDouble
  def length(value: T) = 8

}
package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object BooleanType extends SerializableType {
  val number :Byte = 1
  type T = Boolean
  val kind = classOf[T]
    
  def encode(buffer: ByteBuffer, value: T) {
    val b : Byte = if (value) 1 else 0
    buffer.put( b )
  }

  def decode(buffer: ByteBuffer) = {
    // a 0 byte is false, everything else is true
    buffer.get() != 0
  }

  def length(value: T) = 1
}
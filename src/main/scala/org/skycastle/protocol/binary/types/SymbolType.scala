package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object SymbolType extends SerializableType {
  val number : Byte = 10
  type T = Symbol
  val kind = classOf[T]


  def encode(buffer: ByteBuffer, value: T) = StringType.encode( buffer, value.name )

  def decode(buffer: ByteBuffer) = Symbol( StringType.decode( buffer ) )

  def length(value: T) = StringType.length( value.name )
}
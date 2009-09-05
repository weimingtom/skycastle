package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object SymbolType extends SerializableType {
  val number = 10
  val name = 'symbol
  type T = Symbol

  def encode(buffer: ByteBuffer, value: T) = StringType.encode( buffer, value.name )

  def decode(buffer: ByteBuffer) = Symbol( StringType.decode( buffer ) )

  def length(value: T) = StringType.length( value.name )
}
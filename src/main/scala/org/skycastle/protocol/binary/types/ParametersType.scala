package org.skycastle.protocol.binary.types

import _root_.org.skycastle.util.Parameters
import java.nio.ByteBuffer

/**
 * Handles Parameter objects.
 * 
 * Can also encode and decode Parameters objects with value null.
 */
object ParametersType  extends SerializableType {

  val number : Byte = 15
  type T = Parameters
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) {
    if (value == null) buffer.putInt( -1 )
    else {
      buffer.putInt( value.properties.size )
      value.properties foreach {case (key, value) =>
        SymbolType.encode( buffer, key )
        ObjectType.encode( buffer, value )
      }
    }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    if (numEntries < 0) null
    else {
      var resultMap : Map[Symbol,Object]= Map()
      while (numEntries > 0) {
        val key = SymbolType.decode(buffer)
        val value = ObjectType.decode(buffer)
        val entry = (key, value)
        resultMap = resultMap + entry

        numEntries -= 1
      }

      Parameters( resultMap )
    }
  }


  def length(value: T) = {
    if (value == null) IntType.INT_LEN
    else {
      IntType.INT_LEN +
       lenIterator( value.properties.keys ) +
       lenIterator( value.properties.values ) -
       ObjectType.OBJECT_TYPE_LEN * value.properties.size // We know that all keys are symbols, so that need not be stored
    }
  }

}
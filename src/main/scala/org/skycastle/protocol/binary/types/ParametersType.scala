package org.skycastle.protocol.binary.types

import _root_.org.skycastle.util.Parameters
import java.nio.ByteBuffer

/**
 * 
 */

object ParametersType  extends SerializableType {

  val number = 15
  val name = 'parameters
  type T = Parameters

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.properties.size )
    value.properties foreach {case (key, value) =>
      SymbolType.encode( buffer, key )
      SupportedTypes.encodeObject( buffer, value )
    }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultMap : Map[Symbol,Object]= Map()
    while (numEntries > 0) {
      val key = SymbolType.decode(buffer)
      val value = SupportedTypes.decodeObject(buffer)
      val entry = (key, value)
      resultMap = resultMap + entry

      numEntries -= 1
    }

    Parameters( resultMap )
  }


  def length(value: T) = IntType.INT_LEN +
                         SupportedTypes.lenCollection( value.properties.keys ) +
                         SupportedTypes.lenCollection( value.properties.values ) - 
                         SupportedTypes.OBJECT_TYPE_LEN * value.properties.size // We know that all keys are symbols, so that need not be stored

}
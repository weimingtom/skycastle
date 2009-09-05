package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object MapType extends SerializableType {

  val number = 14
  val name = 'map
  type T = Map[Object,Object]

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.size )
    value foreach {case (key, value) =>
      SupportedTypes.encodeObject( buffer, key )
      SupportedTypes.encodeObject( buffer, value )
    }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultMap : Map[Object,Object]= Map()
    while (numEntries > 0) {
      val key = SupportedTypes.decodeObject(buffer)
      val value = SupportedTypes.decodeObject(buffer)
      val entry = (key, value)
      resultMap = resultMap + entry

      numEntries -= 1
    }

    resultMap
  }

  def length(value: T)  = IntType.INT_LEN +
                          SupportedTypes.lenCollection( value.keys ) +
                          SupportedTypes.lenCollection( value.values )


}
package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object MapType extends SerializableType {

  val number : Byte = 14
  type T = Map[Object,Object]
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.size )
    value foreach {case (key, value) =>
      ObjectType.encode( buffer, key )
      ObjectType.encode( buffer, value )
    }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultMap : Map[Object,Object]= Map()
    while (numEntries > 0) {
      val key = ObjectType.decode(buffer)
      val value = ObjectType.decode(buffer)
      val entry = (key, value)
      resultMap = resultMap + entry

      numEntries -= 1
    }

    resultMap
  }

  def length(value: T)  = IntType.INT_LEN +
                          lenIterator( value.keys ) +
                          lenIterator( value.values )


}
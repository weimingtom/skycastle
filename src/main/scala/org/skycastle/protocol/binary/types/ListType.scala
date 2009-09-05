package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object ListType extends SerializableType {
  val number = 12
  val name = 'list
  type T = scala.List[Object]

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.size )
    value foreach { x => SupportedTypes.encodeObject( buffer, x ) }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultList : List[Object]= Nil
    while (numEntries > 0) {
      val value = SupportedTypes.decodeObject(buffer)
      resultList = resultList ::: List(value)

      numEntries -= 1
    }

    resultList
  }

  def length(value: T) = IntType.INT_LEN + SupportedTypes.lenCollection( value )
  
}
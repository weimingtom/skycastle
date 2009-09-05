package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object ListType extends SerializableType {
  val number : Byte = 12
  type T = scala.List[Object]
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.size )
    value foreach { x => ObjectType.encode( buffer, x ) }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultList : List[Object]= Nil
    while (numEntries > 0) {
      val value = ObjectType.decode(buffer)
      resultList = resultList ::: List(value)

      numEntries -= 1
    }

    resultList
  }

  def length(value: T) = IntType.INT_LEN + lenCollection( value )
  
}
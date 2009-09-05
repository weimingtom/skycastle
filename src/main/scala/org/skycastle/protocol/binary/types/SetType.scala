package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object SetType extends SerializableType {

  val number : Byte = 13
  type T = Set[Object]
  val kind = classOf[T]

  def encode(buffer: ByteBuffer, value: T) {
    buffer.putInt( value.size )
    value foreach { x => SupportedTypes.encodeObject( buffer, x ) }
  }

  def decode(buffer: ByteBuffer) = {
    var numEntries = buffer.getInt()
    var resultSet : Set[Object] = Set()
    while (numEntries > 0) {
      val value = SupportedTypes.decodeObject(buffer)
      resultSet = resultSet + value

      numEntries -= 1
    }

    resultSet
  }

  def length(value: T) = IntType.INT_LEN + SupportedTypes.lenCollection( value )

}
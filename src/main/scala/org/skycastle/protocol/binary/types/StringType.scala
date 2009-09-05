package org.skycastle.protocol.binary.types

import java.nio.ByteBuffer

/**
 * 
 */

object StringType extends SerializableType {

  type T = String
  val number : Byte = 9
  val kind = classOf[T]


  def encode(buffer: ByteBuffer, value: T) {
    // Handle null strings, as EntityId:s could be nulls.
    if (value == null) buffer.putInt( -1 )
    else if (value.length > ProtocolConstants.MAX_STRING_LENGTH_CHARS) {
      ProtocolLogger.logWarning( "Too large string when endocding, "+value.length+" characters.  Replaced with empty string" )
      buffer.putInt( 0 )
    }
    else {
      buffer.putInt( value.length )
      if (value.length > 0) buffer.put( value.getBytes )
    }
  }

  def decode(buffer: ByteBuffer) = {
    val length = buffer.getInt()
    if (length < 0) null
    else if (length == 0) ""
    else if (length > ProtocolConstants.MAX_STRING_LENGTH_CHARS ) {
      ProtocolLogger.logWarning( "Too large string, "+length+" characters.  Replaced with null" )
      null
    }
    else {
      val s = new Array[Byte](length)
      buffer.get( s )
      new String(s)
    }
  }

  def length(value: T) = {
    if (value == null ) IntType.INT_LEN
    else if (value.length > ProtocolConstants.MAX_STRING_LENGTH_CHARS ) IntType.INT_LEN 
    else IntType.INT_LEN + value.length
  }

}

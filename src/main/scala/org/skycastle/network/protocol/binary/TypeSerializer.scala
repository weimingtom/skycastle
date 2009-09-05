package org.skycastle.network.protocol.binary


import java.nio.ByteBuffer

/**
 * Base class for serializers that are specialized at encoding and decoding instances of a specific class
 * from a byte buffer.
 * 
 * @author Hans Haggstrom
 */
abstract class TypeSerializer[TYPE]( kind_ : Class[TYPE]) {

  type T = TYPE

  private val TYPE_ID_LENGTH = 1
  private val NULL_ID : Byte = 1

  var id : Byte = -1
  final def kind : Class[T] = kind_

  final def canSerialize( value : Any ) : Boolean = kind.isInstance( value )

  /**
   * A name representing the kind of objects decoded and encoded by this protocol.
   */
  final def name = kind.getName

  /**
   * Calculates the length of an object to send, in bytes.
   * Used for allocating send buffer.
   * The value can be null.
   */
  final def length( value : T ) : Int = {
    if (value == null) TYPE_ID_LENGTH
    else len( value )
  }

  /**
   * Decodes the next object from the buffer, assuming it is of this type.
   */
  final def decode( buffer : ByteBuffer ) : T = dec(buffer)

  /**
   * Encodes the given object and adds it to the buffer.
   * The value can be null.
   */
  final def encode( buffer : ByteBuffer, value : T ) {
    if (value == null) buffer.put( NULL_ID )
    else enc( buffer, value )
  }


  def len( value : T) : Int
  def enc( buffer : ByteBuffer, value : T )
  def dec( buffer : ByteBuffer ) : T

}


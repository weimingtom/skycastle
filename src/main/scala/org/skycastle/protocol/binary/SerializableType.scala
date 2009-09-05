package org.skycastle.protocol.binary

import binary.types.ObjectType
import java.nio.ByteBuffer

/**
 * Represents some type that the BinaryProtocol can serialize, and provides encoding, decoding, and length calculation methods.
 */
trait SerializableType {

  /**
   * The type of object decoded and encoded by this protocol.
   */
  type T

  /**
   * An unique id number for this protocol.
   */
  val number : Byte

  /**
   * The class of the type of object decoded and encoded by this protocol.
   */
  val kind : Class[T]

  /**
   * A name representing the kind of objects decoded and encoded by this protocol.
   */
  final def name = kind.getName

  /**
   * Calculates the length of an object to send, in bytes.
   * Used for allocating send buffer.
   * The value should not be null.
   */
  def length( value : T ) : Int 

  /**
   * Decodes the next object from the buffer, assuming it is of this type.
   */
  def decode( buffer : ByteBuffer ) : T

  /**
   * Encodes the given object and adds it to the buffer.
   * The object should not be null.
   */
  def encode( buffer : ByteBuffer, value : T )

  /**
   * Utility function that calculates length in bytes of a collection of objects.
   */
  protected def lenCollection( collection : Collection[Object] ) : Int = collection.foldLeft( 0 ){ _ + ObjectType.length( _ ) }

  /**
   * Utility function that calculates length in bytes of an iterator over objects.
   */
  protected def lenIterator( collection : Iterator[Object] )     : Int = collection.foldLeft( 0 ){ _ + ObjectType.length( _ ) }

}
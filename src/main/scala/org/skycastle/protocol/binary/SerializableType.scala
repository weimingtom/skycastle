package org.skycastle.protocol

import java.nio.ByteBuffer

/**
 * Represents some type that the Skycastle Protocols can serialize, and provides encoding, decoding, and length calculation methods.
 */
trait SerializableType {

  type T

  val number : Byte
  val name : Symbol
  final val kind : Class = classOf[T]

  def length( value : T ) : Int
  def decode( buffer : ByteBuffer ) : T
  def encode( buffer : ByteBuffer, value : T )

}
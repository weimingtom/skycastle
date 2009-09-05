package org.skycastle.protocol


import java.nio.ByteBuffer

/**
 * A way of encoding and decoding Messages to and from a stream of bytes.
 * Used for communication between client and server.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
abstract class Protocol  {

  /**
   * The name for this protocol, defined by implementing classes.
   */
  val protocolName : String

  /**
   * Encodes a message to a ByteBuffer
   * Throws an Exception if there was some problem.
   */
  def encode( message : Message) : ByteBuffer

  /**
   * Decodes a message from a ByteBuffer to a Message
   * Throws an Exception if there was some problem.
   */
  def decode( receivedBytes : ByteBuffer  ) : Message
  
}



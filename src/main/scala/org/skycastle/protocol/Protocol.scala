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
   * The name of the protocol, defined by implementing classes.
   */
  val protocolName : String

  /**
   * The name version of the protocol, defined by implementing classes.
   */
  val protocolVersion : Int

  /**
   * Unique identifier for this protocol (consists of name and version).
   */
  def protocolIdentifier : String = protocolName + "-" + protocolVersion

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



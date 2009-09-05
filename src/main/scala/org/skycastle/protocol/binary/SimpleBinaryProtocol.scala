package org.skycastle.protocol

import _root_.org.skycastle.entity.EntityId
import _root_.org.skycastle.util.Parameters
import binary.types.{StringType, ParametersType, EntityIdType}
import java.lang.Class
import java.nio.ByteBuffer

import ProtocolConstants._

/**
 * A straightforward binary protocol without packing.
 */
// TODO: Create one that packs commonly used Symbols with lookup tables / huffman encoding
// TODO: Ints and Longs (and Shorts) could be packed more efficiently if they are close to zero
@serializable
@SerialVersionUID(1)
class SimpleBinaryProtocol extends Protocol {

  val protocolName = "BinaryProtocol"
  val protocolVersion = 1

  def decode(receivedBytes: ByteBuffer) : Message = {
    val calledEntityId = EntityIdType.decode( receivedBytes )
    val actionName = StringType.decode( receivedBytes )
    val parameters = ParametersType.decode( receivedBytes )

    Message( calledEntityId, actionName, parameters )
  }

  def encode(message: Message) : ByteBuffer = {

    val messageLength_bytes = EntityIdType.length( message.calledEntity ) +
                              StringType.length( message.calledAction ) +
                              ParametersType.length( message.parameters )

    val buffer = ByteBuffer.wrap( new Array[Byte]( messageLength_bytes ) );

    EntityIdType.encode( buffer, message.calledEntity )
    StringType.encode( buffer, message.calledAction )
    ParametersType.encode( buffer, message.parameters )

    buffer.flip()

    buffer
  }

}


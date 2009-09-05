package org.skycastle.protocol.binary

import _root_.org.skycastle.entity.EntityId
import _root_.org.skycastle.util.Parameters
import binary.types.{ObjectType, StringType, ParametersType, EntityIdType}
import java.lang.Class
import java.nio.ByteBuffer

import ProtocolConstants._

/**
 * A straightforward binary protocol.
 */
// TODO: Create one that packs commonly used Symbols with lookup tables / huffman encoding
// TODO: Ints and Longs (and Shorts) could be packed more efficiently if they are close to zero
// TODO: Maybe we should allocate some space for the buffer dynamically instead of first calculating the length?
// TODO: We could use a cached buffer array in each protocol that is the size of the maximum allowed size of a message?
@serializable
@SerialVersionUID(1)
class BinaryProtocol extends Protocol {

  val protocolName = "BinaryProtocol"
  val protocolVersion = 1

  def decode(receivedBytes: ByteBuffer) : Message = {
    val calledEntityId = EntityIdType.decode( receivedBytes )
    val actionName = StringType.decode( receivedBytes )
    val parameters = ParametersType.decode( receivedBytes )

    Message( calledEntityId, actionName, parameters )
  }

  def encode(message: Message) : ByteBuffer = {

    if (message.calledEntity == null ) throw new ProtocolException( "Can not send message "+message+" as it doesn't specify an entity to call." )
    if (message.calledAction == null ) throw new ProtocolException( "Can not send message "+message+" as it doesn't specify an action to call." )

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




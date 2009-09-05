package org.skycastle.network.protocol.binary

import _root_.org.skycastle.entity.EntityId
import _root_.org.skycastle.util.Parameters
import java.nio.ByteBuffer


/**
 * A binary message sending protocol.
 */
// TODO: Create one that packs commonly used Symbols with lookup tables
// TODO: We could use a cached buffer array in each protocol that is the size of the maximum allowed size of a message?
// TODO: Maybe we should allocate some space for the buffer dynamically instead of first calculating the length?
@serializable
@SerialVersionUID(1)
class BinaryProtocol extends Protocol {

  val identifier = 'BinaryProtocol

  private val serializer = new BinarySerializer()

  def decode(receivedBytes: ByteBuffer) : List[Message] = {

    var messages : List[Message] = Nil

    // There may be multiple messages in the buffer, decode until it is empty
    while ( receivedBytes.hasRemaining ) {
      val calledEntityId = serializer.decode[EntityId]( receivedBytes )
      val action         = serializer.decode[Symbol]( receivedBytes )
      val parameters     = serializer.decode[Parameters]( receivedBytes )

      messages = messages ::: List( Message( calledEntityId, action, parameters ) )
    }

    messages
  }

  def encode(message: Message) : ByteBuffer = {
    if (message.calledEntity == null ) throw new ProtocolException( "Can not send message "+message+" as it doesn't specify an entity to call." )
    if (message.calledAction == null ) throw new ProtocolException( "Can not send message "+message+" as it doesn't specify an action to call." )

    val messageLength_bytes = serializer.length( message.calledEntity ) +
                              serializer.length( message.calledAction ) +
                              serializer.length( message.parameters )

    val buffer = ByteBuffer.wrap( new Array[Byte]( messageLength_bytes ) );

    serializer.encode( buffer, message.calledEntity )
    serializer.encode( buffer, message.calledAction )
    serializer.encode( buffer, message.parameters )

    buffer.flip()

    buffer
  }

}




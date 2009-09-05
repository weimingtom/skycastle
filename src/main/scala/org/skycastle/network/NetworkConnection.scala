package org.skycastle.network

import _root_.org.skycastle.util.Parameters
import java.nio.ByteBuffer
import negotiator.ProtocolNegotiator
import protocol.{Message, Protocol}

/**
 * Hides protcol negotiation and message merging.
 * The status listener is notified about the negotiation progress, and should take care of closing down the
 * NetworkConnection if it fails.
 */
class NetworkConnection( isServer : Boolean,
                         incomingMessageListener : Message => Unit,
                         outgoingDataListener : ByteBuffer => Unit,
                         onProtocolNegotiationFail : (String, Parameters) => Unit,
                         onProtocolNegotiationSuccess : Parameters => Unit ) {

  private var sendQueue : List[Message] = Nil
  private var fail : Boolean = false
  private var protocol : Protocol = null

  private val negotiator : ProtocolNegotiator = new ProtocolNegotiator( isServer, outgoingDataListener,
    { (p : Protocol, properties : Parameters ) =>
      protocol = p
      sendQueue foreach sendMessage
      sendQueue = Nil
      onProtocolNegotiationSuccess( properties )
    },
    { (failureReason : String, properties : Parameters) =>
      sendQueue = Nil
      fail = true
      onProtocolNegotiationFail(failureReason, properties)
    } )
  

  /**
   * Start protocol negotiations, if it is up to this party to do that.
   */
  def start() {
    negotiator.start()
  }


  def incomingData( buffer : ByteBuffer ) {
    if (fail) {} // Do nothing
    else if ( protocol == null ) {
      // If we haven't agreed on a protocol yet, try
      negotiator.handleData( buffer )
    }
    else {
      // Decode messages and send to message handler
      val messages =  protocol.decode( buffer )
      messages foreach incomingMessageListener
    }
  }

  def sendMessage( message : Message ) {
    if ( fail ) {} // Do nothing
    else if (protocol == null) {
      // If we haven't yet established a protocol, queue the message
      sendQueue = sendQueue ::: List( message )
    }
    else {
      // Encode message and send to outgoing data handler
      outgoingDataListener( protocol.encode( message ) )
    }
  }

}



package org.skycastle.network

import _root_.org.skycastle.entity.EntityId
import java.lang.{IllegalStateException}
import protocol.Protocol
import _root_.org.skycastle.util.Parameters
import java.nio.ByteBuffer
import negotiator.ProtocolNegotiator

/**
 * Hides protcol negotiation and message merging.
 * The status listener is notified about the negotiation progress, and should take care of closing down the
 * NetworkConnection if it fails.
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
final class NetworkConnection( bridgeEntityId : EntityId,
                               isServer : Boolean,
                               parameters : Parameters,
                               incomingMessageListener : Message => Unit,
                               outgoingDataListener : ByteBuffer => Unit,
                               onProtocolNegotiationFail : (String, Parameters) => Unit,
                               onProtocolNegotiationSuccess : Parameters => Unit ) {

  private var networkStarted : Boolean = false
  private var sendQueue : List[Message] = Nil
  private var fail : Boolean = false
  private var protocol : Protocol = null

  private val negotiator : ProtocolNegotiator = new ProtocolNegotiator( isServer, parameters, outgoingDataListener,
    // on success:
    { (p : Protocol, properties : Parameters ) =>
      protocol = p
      p.init( bridgeEntityId )
      sendQueue foreach sendMessage
      sendQueue = Nil
      onProtocolNegotiationSuccess( properties )
    },
    // on failure:
    { (failureReason : String, properties : Parameters) =>
      sendQueue = Nil
      fail = true
      onProtocolNegotiationFail(failureReason, properties)
    } )

  def isStarted : Boolean = networkStarted
  
  /**
   * Start protocol negotiations, if it is up to this party to do that.
   */
  def start() {
    if (!networkStarted) {
      networkStarted = true
      negotiator.start()
    }
    else throw new IllegalStateException( "Network is already started." )
  }

  /**
   * Stop the network.  The network may be started again later.
   * Queued messages will be lost.
   */
  def stop() {
    if ( networkStarted) {
      negotiator.stop()
      networkStarted = false
      sendQueue = Nil
      fail = false
      protocol = null
    }
  }


  def handleIncomingData( buffer : ByteBuffer ) {
    if (!networkStarted || fail) {} // Do nothing
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
    if ( !networkStarted || fail ) {} // Do nothing
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



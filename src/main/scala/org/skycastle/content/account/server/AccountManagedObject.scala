package org.skycastle.content.account.server

import _root_.org.skycastle.network.{Message, NetworkConnection}
import _root_.org.skycastle.server.ManagedEntity
import _root_.org.skycastle.util.Parameters
import com.sun.sgs.app._
import java.nio.ByteBuffer

/**
 * 
 */
@serializable
@SerialVersionUID(1)
class AccountManagedObject( accountEntity : ServerSideAccountEntity )
        extends ManagedEntity[ServerSideAccountEntity]( accountEntity ) with ClientSessionListener with AccountConnectionHandler {

  private var currentSessionRef : ManagedReference[ClientSession] = null

  private var network : NetworkConnection = null

  accountEntity.setAccountConnection( this )

  def init() {
    network = new NetworkConnection(  accountEntity.id,
                                      true,
                                      onMessagFromClientToServer,
                                      sendDataToClient,
                                      onProtocolNegotiationFail,
                                      onProtocolNegotiationSuccess )
  }

  def setSession( session : ClientSession ) {
    markThisForUpdate

    if (currentSessionRef != null) {
      accountEntity.logWarning( "New ClientSession specified although we still have a reference to the old client session.  Replacing old with new." )
    }

    currentSessionRef = AppContext.getDataManager.createReference(session)

    accountEntity.setAccountName( session.getName )
    accountEntity.logInfo( "Connected to session " + session )

    network.start
  }


  def onMessagFromClientToServer( message : Message ) {
    accountEntity.onMessageFromClient( message )
  }

  def sendMessage( message : Message  ) {
    network.sendMessage( message )
  }

  def receivedMessage(buffer: ByteBuffer) {
    network.handleIncomingData( buffer )
  }

  def sendDataToClient(buffer: ByteBuffer) {
    if (currentSessionRef != null) {
      currentSessionRef.get.send( buffer )
    }
    else {
      accountEntity.logDebug( "Tried to send data to client although no client is connected.  Message ignored." )
    }
  }


  def disconnect() {
    val dataManager : DataManager = AppContext.getDataManager()
    dataManager.removeObject( currentSessionRef.get )

    markThisForUpdate
    currentSessionRef = null
  }



  def onProtocolNegotiationFail( failureReason : String, serverProperties : Parameters ) {
    handleDisconnect( true, "protocol negotiation failed, closing connection", failureReason )
  }

  def onProtocolNegotiationSuccess(clientParameters : Parameters) {
    accountEntity.onConnected( clientParameters )
  }

  def disconnected(graceful: Boolean) {
    handleDisconnect( false, "disconnected", "" )
  }

  def markThisForUpdate {
    val dataManager : DataManager = AppContext.getDataManager()
    dataManager.markForUpdate(this)
  }

  private def handleDisconnect( logout : Boolean, reason : String, underlyingReason : String ) {
    if (currentSessionRef != null) {
      markThisForUpdate

      currentSessionRef = null
      network.stop
      if (logout) disconnect
      accountEntity.onDisconnected( reason + " : " + underlyingReason )
    }
  }
  



}
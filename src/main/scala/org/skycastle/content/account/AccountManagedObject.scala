package org.skycastle.content.account

import _root_.org.skycastle.network.{Message, NetworkConnection}
import _root_.org.skycastle.server.ManagedEntity
import _root_.org.skycastle.util.Parameters
import com.sun.sgs.app._
import java.net.PasswordAuthentication
import java.nio.ByteBuffer

/**
 * 
 */
class AccountManagedObject( accountEntity : ServerSideAccountEntity )
        extends ManagedEntity[ServerSideAccountEntity]( accountEntity ) with ClientSessionListener {

  private var currentSessionRef : ManagedReference[ClientSession] = null

  private var connectionStarted : Boolean = false

  private val network = new NetworkConnection(  true,
                                                onMessage,
                                                simpleClient.send,
                                                onProtocolNegotiationFail,
                                                onConnected )


/*
  protected def sendDataToServer( data : ByteBuffer ) {
    simpleClient.send( data )
  }
*/

  def setSession( session : ClientSession ) {
    val dataManager : DataManager = AppContext.getDataManager()
    dataManager.markForUpdate(this)
    currentSessionRef = dataManager.createReference(session)

    accountEntity.logInfo( "Connected to session " + session )
  }

  def onMessage( message : Message ) {
    
  }


  def disconnect() {
    if (connectionStarted) simpleClient.logout(false)
  }


  def receivedMessage(buffer: ByteBuffer) {
    network.handleIncomingData( buffer )
  }


  def loggedIn {
    network.start
  }


  def onProtocolNegotiationFail( failureReason : String, serverProperties : Parameters ) {
    handleDisconnect( true, "protocol negotiation failed", failureReason )
  }

  def disconnected(p1: Boolean, reason: String) {
    handleDisconnect( false, "disconnected", reason )
  }

  def loginFailed(reason: String) {
    handleDisconnect( false, "login failed", reason )
  }


  private def handleDisconnect( logout : Boolean, reason : String, underlyingReason : String ) {
    if (connectionStarted) {
      connectionStarted = false
      network.stop
      if (logout) simpleClient.logout( false )
      onDisconnected( reason + " : " + underlyingReason )
    }
  }
  

  def receivedMessage(message: ByteBuffer) = {}

  def disconnected(graceful: Boolean) = {}


}
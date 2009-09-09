package org.skycastle.content.account.server


import _root_.org.skycastle.content.account.client.ClientSideAccountEntity
import _root_.org.skycastle.entity.{EntityId, Entity}
import network.Message
import util.Parameters

object ServerSideAccountEntity {
  val SERVER_ACCOUNT_ID = EntityId( "ServerAccount" )
}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ServerSideAccountEntity extends Entity {

  def accountName : String = myAccountName


  private var myAccountName : String = null
  private var accountConnection : AccountConnectionHandler = null

  final def sendMessageToClient( message : Message ) {
    logInfo( "Sending message to client: " + message )
    accountConnection.sendMessage( message )
  }

  final def disconnect() {
    accountConnection.disconnect
  }

  def onMessageFromClient( message : Message )  {
    logInfo( "Received message from client: " + message )

    // Dispatch messages addressed to SERVER_ACCOUNT_ID to self
    val messageToSend = if (message.calledEntity == ServerSideAccountEntity.SERVER_ACCOUNT_ID )
      Message( id, message.calledAction, message.parameters )
    else
      message

    // Dispatch to the called entity.
    // The called entity will need to allow this entity to execute the action for it to actually take place.
    callOtherEntity( messageToSend )
  }

  def onDisconnected( reason : String ) {
    logInfo( "Connection to client failed.  Reason: " + reason )
  }

  def onConnected( clientParameters : Parameters ) {
    logInfo( "Connected to client.  Client properties are: " + clientParameters )

    sendMessageToClient( Message( ClientSideAccountEntity.CLIENT_ACCOUNT_ID, 'hiThereClient, Parameters( 'bar -> "zap" ) ) )
  }


  final def setAccountName( name : String ) {
    myAccountName = name
  }

  final def setAccountConnection( connection : AccountConnectionHandler ) {
    accountConnection = connection
  }



}


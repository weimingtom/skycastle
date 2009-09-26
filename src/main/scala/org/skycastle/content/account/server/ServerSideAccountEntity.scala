package org.skycastle.content.account.server


import _root_.org.skycastle.content.account.client.ClientSideAccountEntity
import _root_.org.skycastle.entity.{EntityId, Entity}
import network.Message
import util.Parameters


/**
 *
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ServerSideAccountEntity extends Entity {

  def accountName : String = myAccountName

  var clientMainEntityId : EntityId = null

  private var myAccountName : String = null
  private var accountConnection : AccountConnectionHandler = null

  final def sendMessageToClient( message : Message ) {
    logInfo( "Sending message to client: " + message )
    accountConnection.sendMessage( message )
  }

  final def disconnect() {
    accountConnection.disconnect
  }

  override def callContained(caller: EntityId, innerEntityId: EntityId, actionId: Symbol, parameters: Parameters) {
    // TODO: Check if the caller has the right to send messages to the client?

    sendMessageToClient( Message( caller, innerEntityId, actionId, parameters ) )
  }

  def onMessageFromClient( message : Message )  {
    logInfo( "Received message from client: " + message )

    // Dispatch to the called entity.
    // The called entity will need to allow this entity to execute the action for it to actually take place.
    callOtherEntity( message )
  }

  def onDisconnected( reason : String ) {
    logInfo( "Connection to client failed.  Reason: " + reason )
  }

  def onConnected( clientParameters : Parameters ) {
    logInfo( "Connected to client.  Client properties are: " + clientParameters )

    clientMainEntityId = clientParameters.getEntityId( 'mainEntity, null )
  }


  final def setAccountName( name : String ) {
    myAccountName = name
  }

  final def setAccountConnection( connection : AccountConnectionHandler ) {
    accountConnection = connection
  }



}


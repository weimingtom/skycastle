package org.skycastle.content.account.client


import _root_.org.skycastle.content.account.server.ServerSideAccountEntity
import _root_.org.skycastle.util.Parameters
import activity.ActivityClient
import entity.{parameters, EntityId, Entity}
import java.net.{PasswordAuthentication}
import java.lang.String
import network.{Message}


/**
 * A client side Entity that represents an user account on a specific server.
 * Any messages from the server are forwarded by this Entity with this Entity as the sender to the receipients.
 *
 * @author Hans Haggstrom
 */
// TODO: Merge similar parts of ClientSideAccountEntity and ServerSideAccountEntity
final class ClientSideAccountEntity extends Entity {
  var server: String = "localhost"
  var port: String = "1139"
  var accountName: String = null

  var serverMainActivity : EntityId = null


  @transient private var password: Array[Char] = null
  private var connected: Boolean = false
  private var clientNetwork : ClientNetwork = null

  private val DEFAULT_PORT = "1139"

  def isConnected: Boolean = connected


  override protected def onInit( creationParameters : Parameters ) {
    clientNetwork = new ClientNetwork( id, creationParameters, onMessage, onConnected, onDisconnected, getPassword)
  }

  def connect() {
    val actualPort = if (port == null) DEFAULT_PORT else port

    logInfo("Trying to connect to server at " + server + ":" + actualPort + ".")
    clientNetwork.connect(server, actualPort)
  }

  def disconnect() {
    logInfo("Trying to disconnect from server")
    clientNetwork.disconnect
  }




  override def callContained(message : Message) {
    // TODO: Check if the caller has the right to send messages to the server?

    sendMessageToServer( message )
  }


  /**
   * Sends a message to the server side
   */
  def sendMessageToServer(message: Message) {
    clientNetwork.sendMessage(message)
  }

  /**
   * Called when the client has successfully connected to a server, and messages can be sent.
   * The provided parameters are the publicly visible server properties (e.g. version, name, description, etc).
   */
  def onConnected(serverProperties: Parameters) {
    connected = true

    logInfo("Connected to server.  Server properties are: " + serverProperties)

    val entityIdString = serverProperties.getString( 'mainActivity, null )

    if (entityIdString == null) {
      serverMainActivity = null
      logError( "The server specified no id for a mainActivity to connect to!" )
    }
    else {
      serverMainActivity = new EntityId( id.path ::: List( entityIdString ) )
      // Create activity client entity to show the activity UI and provide the activity with user input.
      // TODO: Give the client a panel / tab in the UI
      val client = new ActivityClient()
      container.storeEntity( client, Parameters( 'activityId -> serverMainActivity ) )
    }
  }

  /**
   * Called if a connection attempt fails, or if an established connection fails.
   */
  def onDisconnected(reason: String) {
    connected = false

    logInfo("Connection to server failed.  Reason: " + reason)
  }


  def setPassword(password_ : Array[Char]) {
    password = password_
  }


  private def getPassword(): PasswordAuthentication = {
    new PasswordAuthentication(accountName, password)
  }

  private def onMessage(message: Message) {

    logInfo( "Received message from server: " + message )

/*
    // Dispatch messages addressed to CLIENT_ACCOUNT_ID to self
    val messageToSend = if (message.calledEntity == ClientSideAccountEntity.CLIENT_ACCOUNT_ID )
      Message( message.callingEntity, id, message.calledAction, message.parameters )
    else
      message
*/

    // Dispatch to the called entity.
    // The called entity will need to allow this entity to execute the action for it to actually take place.
    container.call( message )

  }


}


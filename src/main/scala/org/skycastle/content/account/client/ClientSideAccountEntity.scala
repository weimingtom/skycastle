package org.skycastle.content.account.client


import _root_.org.skycastle.content.account.server.ServerSideAccountEntity
import _root_.org.skycastle.entity.{EntityId, Entity}
import _root_.org.skycastle.util.Parameters
import java.net.{PasswordAuthentication}
import java.lang.String
import network.{Message}

object ClientSideAccountEntity {

  /**
   * Entity id that is treated as this entity when any message to it is received from the server.
   */
  val CLIENT_ACCOUNT_ID = EntityId( "ClientAccount" )
}

/**
 * A client side Entity that represents an user account on a specific server.
 * Any messages from the server are forwarded by this Entity with this Entity as the sender to the receipients.
 *
 * @author Hans Haggstrom
 */
final class ClientSideAccountEntity extends Entity {
  var server: String = "localhost"
  var port: String = "1139"
  var accountName: String = null


  @transient private var password: Array[Char] = null
  private var connected: Boolean = false
  private var clientNetwork : ClientNetwork = null

  private val DEFAULT_PORT = "1139"

  def isConnected: Boolean = connected


  override protected def onInit() {
    clientNetwork = new ClientNetwork( id, onMessage, onConnected, onDisconnected, getPassword)
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



  override def callContained(caller: EntityId, innerEntityId: EntityId, actionId: Symbol, parameters: Parameters) {
    // TODO: Check if the caller has the right to send messages to the server?

    sendMessageToServer( Message( caller, innerEntityId, actionId, parameters ) )
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

    sendMessageToServer( Message( id, ServerSideAccountEntity.SERVER_ACCOUNT_ID, 'helloServer, Parameters( 'foo -> "bar" ) ) )
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

    // Dispatch messages addressed to CLIENT_ACCOUNT_ID to self
    val messageToSend = if (message.calledEntity == ClientSideAccountEntity.CLIENT_ACCOUNT_ID )
      Message( message.callingEntity, id, message.calledAction, message.parameters )
    else
      message

    // Dispatch to the called entity.
    // The called entity will need to allow this entity to execute the action for it to actually take place.
    callOtherEntity( messageToSend )

  }


}


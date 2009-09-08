package org.skycastle.content.account.client


import _root_.org.skycastle.util.Parameters
import java.net.{PasswordAuthentication}
import java.lang.String
import entity.Entity
import network.{Message}

/**
 * A client side Entity that represents an user account on a specific server.
 * Any messages from the server are forwarded by this Entity with this Entity as the sender to the receipients.
 * 
 * @author Hans Haggstrom
 */
final class ClientSideAccountEntity extends Entity {

  var server : String = "localhost"
  var port : String = "1139"
  var accountName : String = null

  @transient private var password : Array[Char] = null
  private var connected : Boolean = false
  private val clientNetwork = new ClientNetwork( onMessage,  onConnected, onDisconnected, getPassword )

  private val DEFAULT_PORT = "1139"

  def isConnected : Boolean = connected

  def connect() {
    val actualPort = if (port == null) DEFAULT_PORT else port

    logInfo( "Trying to connect to server at "+server+":"+actualPort+"." )
    clientNetwork.connect( server, actualPort )
  }

  def disconnect() {
    logInfo( "Trying to disconnect from server" )
    clientNetwork.disconnect
  }

  /**
   * Called when the client has successfully connected to a server, and messages can be sent.
   * The provided parameters are the publicly visible server properties (e.g. version, name, description, etc).
   */
  def onConnected( serverProperties : Parameters ) {
    connected = true

    logInfo( "Connected to server.  Server properties are: " + serverProperties )
  }

  /**
   * Called if a connection attempt fails, or if an established connection fails.
   */
  def onDisconnected( reason : String ) {
    connected = false

    logInfo( "Connection to server failed.  Reason: " + reason )
  }


  def setPassword( password_ : Array[Char] ) {
    password = password_
  }

  
  private def getPassword() : PasswordAuthentication = {
    new PasswordAuthentication( accountName, password )
  }

  private def onMessage( message : Message ) {
    // Dispatch to the called entity.
    // The called entity will need to allow this entity to execute the action for it to actually take place.
    callOtherEntity( message )
  }


}


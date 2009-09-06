package org.skycastle.content.account


import _root_.org.skycastle.util.Parameters
import com.sun.sgs.client.ClientChannel
import com.sun.sgs.client.simple.{SimpleClient, SimpleClientListener}
import java.net.{URL, PasswordAuthentication}
import java.nio.ByteBuffer
import java.lang.String
import entity.Entity
import java.util.Properties
import network.{Message, NetworkConnection}

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

  def isConnected : Boolean = connected

  def connect() {
    clientNetwork.connect( server, port )
  }

  def disconnect() {
    clientNetwork.disconnect
  }

  /**
   * Called when the client has successfully connected to a server, and messages can be sent.
   * The provided parameters are the publicly visible server properties (e.g. version, name, description, etc).
   */
  def onConnected( serverProperties : Parameters ) {
    connected = true
  }

  /**
   * Called if a connection attempt fails, or if an established connection fails.
   */
  def onDisconnected( reason : String ) {
    connected = false
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


package org.skycastle.content.account


import com.sun.sgs.client.ClientChannel
import com.sun.sgs.client.simple.{SimpleClient, SimpleClientListener}
import java.net.{URL, PasswordAuthentication}
import java.nio.ByteBuffer
import java.lang.String
import entity.Entity
import java.util.Properties
import network.Message

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final class ClientSideAccountEntity extends Entity  with SimpleClientListener{

  var server : String = "localhost"
  var port : String = "1139"
  var accountName : String = null

  val simpleClient = new SimpleClient( this )

  @transient
  private var password : Array[Char] = null

  def setPassword( password_ : Array[Char] ) {
    password = password_
  }

  def connect() {
    val connectProps = new Properties()
    connectProps.put("host", server)
    connectProps.put("port", port)

    try {
      simpleClient.login( connectProps )
    } catch {
      case e : Exception =>
      logError( "Problem when trying to connect to "+server+":"+port+" : " + e, e )
    }
  }

  def disconnect() {

  }

  def sendMessageToServer( message : Message ) {

  }

  def onMessageFromServer( message : Message )  {

  }



  def reconnected = {}

  def disconnected(p1: Boolean, p2: String) {

  }

  def receivedMessage(buffer: ByteBuffer) {
    

  }

  def joinedChannel(channel: ClientChannel) = null

  def reconnecting = {}

  def getPasswordAuthentication : PasswordAuthentication = {
    new PasswordAuthentication(accountName, password)
  }

  def loggedIn {

  }

  def loginFailed(reason: String) {

  }
}


package org.skycastle.content.account

import _root_.com.sun.sgs.client.simple.{SimpleClient, SimpleClientListener}
import _root_.com.sun.sgs.client.{ClientChannelListener, ClientChannel}
import _root_.org.skycastle.util.Parameters
import java.net.PasswordAuthentication
import java.nio.ByteBuffer
import java.lang.String
import java.util.Properties
import network.{Message, NetworkConnection}
/**
 * 
 */

class ClientNetwork( onMessage : Message => Unit,
                     onConnected : Parameters => Unit,
                     onDisconnected : String => Unit,
                     getPassword : => PasswordAuthentication ) extends SimpleClientListener{

  private var connectionStarted : Boolean = false

  private val simpleClient = new SimpleClient( this )

  private val network = new NetworkConnection(  false,
                                                onMessage,
                                                simpleClient.send,
                                                onProtocolNegotiationFail,
                                                onConnected )


/*
  protected def sendDataToServer( data : ByteBuffer ) {
    simpleClient.send( data )
  }
*/


  def connect( host : String, port : String ) {
    if (!connectionStarted) {
      connectionStarted = true

      val connectProps = new Properties()
      connectProps.put("host", host)
      connectProps.put("port", port)

      try {
        simpleClient.login( connectProps )
      } catch {
        case e : Exception =>
          handleDisconnect( false, "could not create connection to "+host+":"+port, e.getMessage )
      }
    }
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

  def getPasswordAuthentication : PasswordAuthentication = getPassword


  def onProtocolNegotiationFail( failureReason : String, serverProperties : Parameters ) {
    handleDisconnect( true, "protocol negotiation failed", failureReason )
  }

  def disconnected(p1: Boolean, reason: String) {
    handleDisconnect( false, "disconnected", reason )
  }
  
  def loginFailed(reason: String) {
    handleDisconnect( false, "login failed", reason )
  }

  
  def joinedChannel(channel: ClientChannel) : ClientChannelListener = null

  def reconnecting = {}

  def reconnected = {}

  private def handleDisconnect( logout : Boolean, reason : String, underlyingReason : String ) {
    if (connectionStarted) {
      connectionStarted = false
      network.stop
      if (logout) simpleClient.logout( false )
      onDisconnected( reason + " : " + underlyingReason )
    }
  }

}
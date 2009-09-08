package org.skycastle.content.account.server


import entity.Entity
import network.Message
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

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
  }

  def onDisconnected( reason : String ) {
    logInfo( "Connection to client failed.  Reason: " + reason )
  }

  def onConnected( clientParameters : Parameters ) {
    logInfo( "Connected to client.  Client properties are: " + clientParameters )

  }


  final def setAccountName( name : String ) {
    myAccountName = name
  }

  final def setAccountConnection( connection : AccountConnectionHandler ) {
    accountConnection = connection
  }



}


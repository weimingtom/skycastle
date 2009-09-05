package org.skycastle.content.account


import entity.Entity
import java.net.URL
import protocol.Message

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final class ClientSideAccountEntity extends Entity {

  var serverUrl : URL = null
  var accountName : String = null

  @transient
  private var password : Array[Char] = null

  def setPassword( password_ : Array[Char] ) {
    password = password_
  }

  def connect() {
    
  }

  def disconnect() {

  }

  def sendMessageToServer( message : Message ) {

  }

  def onMessageFromServer( message : Message )  {

  }

  def onDisconnected() {

  }

  def onConnected() {

  }

}


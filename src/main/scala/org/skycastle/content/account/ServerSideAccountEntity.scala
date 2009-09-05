package org.skycastle.content.account


import entity.Entity
import protocol.Message

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class ServerSideAccountEntity extends Entity {

  var accountName : String = null


  def sendMessageToClient( message : Message ) {

  }

  def onMessageFromClient( message : Message )  {

  }

  def onDisconnected() {

  }

  def disconnect() {

  }

}


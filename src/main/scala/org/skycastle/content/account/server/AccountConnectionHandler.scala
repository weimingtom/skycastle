package org.skycastle.content.account.server


import network.Message

/**
 * 
 * 
 * @author Hans Haggstrom
 */
trait AccountConnectionHandler {

  /**
   * Disconnect from the connected party.
   */
  def disconnect()

  /**
   *  Send a message over the connection.
   */
  def sendMessage( message : Message  )


}
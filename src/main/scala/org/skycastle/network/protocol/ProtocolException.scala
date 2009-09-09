package org.skycastle.network.protocol

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ProtocolException(description : String) extends Exception(description) {

  // dummy method for testing
  def getDescription = description
}


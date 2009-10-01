package org.skycastle.entity

object CallException {

  def apply( message : String ) = new CallException(message, null)
}

/**
 * An exception used to signal that there was some problem when executing a call on an Entity.
 * 
 * @author Hans Haggstrom
 */
case class CallException(message : String, reason : Throwable) extends Exception(message, reason) {
  
}
  
package org.skycastle.util


import java.util.logging.Level

/**
 * A mixin that provides various logging methods that call a generic logging method.
 * 
 * @author Hans Haggstrom
 */
// TODO: Use own level names for severe and fine, or official, or both?
trait LogMethods {
  def logError( message : => String ) = log( Level.SEVERE, message )
  //def severe( message : => String ) = log( Level.SEVERE, message )
  def logWarning( message : => String ) = log( Level.WARNING, message )
  def logInfo( message : => String ) = log( Level.INFO, message )
  def logDebug( message : => String ) = log( Level.FINE, message )
  //def fine( message : => String ) = log( Level.FINE, message )
  def logTrace( message : => String )= log( Level.FINER, message )
  //def finer( message : => String )= log( Level.FINER, message )

  def logError( message : => String, exception : => Throwable ) = log( Level.SEVERE, message, exception )
  //def severe( message : => String, exception : => Throwable ) = log( Level.SEVERE, message, exception )
  def logWarning( message : => String, exception : => Throwable ) = log( Level.WARNING, message, exception )
  def logInfo( message : => String, exception : => Throwable ) = log( Level.INFO, message, exception )
  def logDebug( message : => String, exception : => Throwable ) = log( Level.FINE, message, exception )
  //def fine( message : => String, exception : => Throwable ) = log( Level.FINE, message, exception )
  def logTrace( message : => String , exception : => Throwable )= log( Level.FINER, message, exception )
  //def finer( message : => String , exception : => Throwable )= log( Level.FINER, message, exception )

  def log( level : Level, message : => String ) : Unit = log( level, message, null )
  def log( level : Level, message : => String , exception : => Throwable ) : Unit

}


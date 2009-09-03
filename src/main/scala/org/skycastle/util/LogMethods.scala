package org.skycastle.util


import java.util.logging.{Level, Logger}

/**
 * A mixin that provides various logging methods that call a generic logging method.
 * 
 * Utilizes scalas function passing syntax to only evaluate parameters if the message should be logged.
 *
 * @author Hans Haggstrom
 */
// TODO: Use own level names for severe and fine, or official, or both?
// TODO: Does this mean that we instead create a lot of instances of abstract classes that are passed to the log methods?
trait LogMethods {

  def log( level : Level, message : => String , exception : => Throwable )


  final def logError( message : => String ) { log( Level.SEVERE, message ) }
  //def severe( message : => String ) = log( Level.SEVERE, message )
  final def logWarning( message : => String ) { log( Level.WARNING, message ) }
  final def logInfo( message : => String ) { log( Level.INFO, message ) }
  final def logDebug( message : => String ) { log( Level.FINE, message ) }
  //def fine( message : => String ) = log( Level.FINE, message )
  final def logTrace( message : => String ) { log( Level.FINER, message ) }
  //def finer( message : => String )= log( Level.FINER, message )

  final def logError( message : => String, exception : => Throwable ) { log( Level.SEVERE, message, exception ) }
  //def severe( message : => String, exception : => Throwable ) = log( Level.SEVERE, message, exception )
  final def logWarning( message : => String, exception : => Throwable ) { log( Level.WARNING, message, exception ) }
  final def logInfo( message : => String, exception : => Throwable ) { log( Level.INFO, message, exception ) }
  final def logDebug( message : => String, exception : => Throwable ) { log( Level.FINE, message, exception ) }
  //def fine( message : => String, exception : => Throwable ) = log( Level.FINE, message, exception )
  final def logTrace( message : => String , exception : => Throwable ) { log( Level.FINER, message, exception ) }
  //def finer( message : => String , exception : => Throwable )= log( Level.FINER, message, exception )

  final def log( level : Level, message : => String ) : Unit = { log( level, message, null ) }


}


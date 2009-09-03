package org.skycastle.util


import java.util.logging.{Level, Logger}

/**
 * A logger that utilizes scalas function passing syntax to only evaluate parameters if the message should be logged.
 * 
 * @author Hans Haggstrom
 */
// TODO: Does this mean that we instead create a lot of instances of abstract classes that are passed to the log methods?
// TODO: Use own level names for severe and fine, or official, or both?
class LazyLogger( loggerLocation : String ) extends LogMethods {

  val logger = Logger.getLogger( loggerLocation )
  
  private val offValue = Level.OFF.intValue

  def log( level : Level, message : => String , exception : => Throwable ) {

    if (level.intValue() >= logger.getLevel.intValue && logger.getLevel.intValue != offValue ) {
      val e = exception
      if (e == null) logger.log( level, message )
      else logger.log( level, message, e )
    }

  }
}


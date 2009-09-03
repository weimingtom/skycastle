package org.skycastle.util

import java.util.logging.{Level, Logger}

/**
 * 
 */

class LogUtil(path : String) extends LogMethods {

  val logger : Logger = Logger.getLogger( path )

  def log(level: Level, message: => String, exception: => Throwable)  {

    if (logger != null && logger.isLoggable(level) )
    {
      logger.log( level, message, exception )
    }
  }

}
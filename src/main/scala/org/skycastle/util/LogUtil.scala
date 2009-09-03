package org.skycastle.util

import java.util.logging.{Level, Logger}

/**
 * A logging utility that provides the logging methods in LogMethods and logs to a Logger with a specified path. 
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
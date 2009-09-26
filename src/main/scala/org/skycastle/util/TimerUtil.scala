package org.skycastle.util

/**
 * Timing related utility functions.
 */
object TimerUtil {

  val timerLogger = new LogUtil("org.skycastle.util.TimerUtil")

  /**
   * Measures the duration of executing a block in milliseconds, using nanosecond precision,
   * and prints the resulting time to the log at info level.
   */
  def timeAndLog[T]( description : String )( block : => T) : T = {
    val startTime : Long = System.nanoTime

    val result = block

    val endTime : Long = System.nanoTime

    val totalTimeMs = (endTime - startTime) / 1000000.0

    timerLogger.logInfo( "Duration of "+description+": " + totalTimeMs + " ms" )

    result
  }

  /**
   * Measures the duration of executing a block in milliseconds, using nanosecond precision.
   */
  def time( block : => Unit ) : Double = {
    val startTime : Long = System.nanoTime

    block

    val endTime : Long = System.nanoTime

    (endTime - startTime) / 1000000.0
  }


}
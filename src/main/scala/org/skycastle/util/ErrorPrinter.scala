package org.skycastle.util

/**
 * 
 * 
 * @author Hans Haggstrom
 */

object ErrorPrinter {
  def prettyPrint( description : String, e : Exception ) : String = {
    description + ", error: " + e + "\n" + e.getStackTrace.mkString("\n") + "\n"
  }
}


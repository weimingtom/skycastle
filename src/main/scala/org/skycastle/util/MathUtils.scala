package org.skycastle.util

/**
 * 
 * 
 * @author Hans Haggstrom
 */

object MathUtils {

  def clamp( a : Int, min : Int, max : Int ) = if (a < min) min else if (a > max) max else a
  def clamp( a : Float, min : Float, max : Float ) = if (a < min) min else if (a > max) max else a
  def inRange( a : Int, min : Int, max : Int ) = a >= min && a < max

}


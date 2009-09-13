package org.skycastle.util

/**
 * Provides utility functions for checking prameter preconditions.
 *
 * Import e.g. with ParameterChecker._ to minimize syntactic noise.
 *
 * Uses the scala require function as a backend.
 * 
 * @author Hans Haggstrom
 */
object ParameterChecker {

  def requireNotNull( value : Any, parameterName : Symbol ) {
    require( value != null, "The parameter '"+parameterName.name+"' should not be null, but it was."  )
  }

}


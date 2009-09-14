package org.skycastle.util

/**
 * Provides utility functions for checking prameter preconditions.
 *
 * Import e.g. with ParameterChecker._ to minimize syntactic noise.
 *
 * Throws IllegalArgumentException with a user readable message if the requirement fails.
 *
 * @author Hans Haggstrom
 */
object ParameterChecker {

  def requireNotNull( value : Any, parameterName : Symbol ) {
    test( value != null, value, parameterName, "not be null" )
  }

  def requireNotEmpty( value : Iterable[_], parameterName : Symbol ) {
    requireNotNull( value, parameterName )

    test( !value.isEmpty, value, parameterName, "not be empty" )
  }

  /**
   * Should be a valid identifier (letter or underscore or dollar followed by the same or numbers).
   */
  def requireIsIdentifier( s : String, parameterName : Symbol ) {

    test( StringUtils.isIdentifier( s ), s, parameterName, "be a valid identifier" )
  }


  private def test( testResult : Boolean, value : Any, parameter : Symbol, requirement : String ) {
    if ( !testResult ) {
      val stringValue = if (value == null) "null" else value.toString
      throw new IllegalArgumentException( "The parameter '"+parameter.name+"' should "+requirement+", but it was '"+stringValue+"'."  )
    }
  }

}


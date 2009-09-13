package org.skycastle.util


import java.io.Serializable

object Parameters {

  def apply (  elems: (Symbol, Any)*) = {
    if (elems.length == 0) empty
    else new Parameters( Map.empty ++ elems )
  }

  val empty = new Parameters( Map.empty )

  val KEY_VALUE_SEPARATOR = '='
  val ENTRY_SEPARATOR = '\n'

  /**
   * Creates a Parameters object from a string containing key = value entries separated by the equals sign,
   * and where the entries are separated by newlines.
   */
  def fromKeyValueString( keyValueList : String ) : Parameters = {
    var properties : Map[Symbol, String] = Map()

    val rows = List.fromString( keyValueList, ENTRY_SEPARATOR )

    rows foreach { row : String =>

      val splitIndex = row.indexOf( KEY_VALUE_SEPARATOR )

      if ( splitIndex > 0 && splitIndex + 1 < row.length ) {

        val key = (row.substring(0, splitIndex)).trim
        val value = (row.substring(splitIndex + 1)).trim

        if (!key.isEmpty) {
          val entry = (Symbol(key), value)
          properties = properties + entry
        }

      }
    }

    new Parameters( properties )
  }


}

/**
 * An immutable set of named properties.
 * 
 * NOTE: All parameter contents should be serializable
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class Parameters(val entries : Map[Symbol, Any]) extends TypedGetters {

  def ++ ( otherParameters : Parameters ) : Parameters = {
    val newEntries = entries ++ otherParameters.entries
    Parameters( newEntries )
  }

  def + ( entry : (Symbol, Any) ) : Parameters = {
    val newEntries = entries + entry
    Parameters( newEntries )
  }

  def add( key : Symbol, value : Any  ) : Parameters = {
    val entry = (key, value)
    this + entry
  }

}
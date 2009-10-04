package org.skycastle.util

import _root_.scala.runtime.RichString
import java.nio.ByteBuffer
import sun.misc.Regexp
import java.util.regex.Pattern

/**
 * String related utility functions
 */

object StringUtils {

  private val ENCODING_CHARSET = "UTF-8"

  /**
   * Decodes a ByteBuffer into a {@code String}, using the UTF-8 encoding.
   */
  def decodeString( buffer: ByteBuffer ) : String = {
    val stringBytes = new Array[Byte]( buffer.capacity() )
    buffer.get( stringBytes )
    new String( stringBytes, ENCODING_CHARSET )
  }

  /**
   * Encodes a {@code String} into a ByteBuffer, using the UTF-8 encoding.
   */
  def encodeString( s : String ) : ByteBuffer = {
    ByteBuffer.wrap( s.getBytes( ENCODING_CHARSET ) )
  }


  /**
   * True if s is a java style identifier (starts with letter, followed by numbers and letters, should be non-empty).
   */
  def isIdentifier( s : String ) : Boolean = {
    if (s == null || s.length <= 0) false
    else if ( !Character.isJavaIdentifierStart( s.charAt( 0 ) ) ) false
    else {
      s forall Character.isJavaIdentifierPart
    }
  }

  /**
   * Converts a list of comma separated tokens in a string to a list of Symbols.
   */
  def commaSeparatedStringToSymbolList( s : String) : List[Symbol] = {
    List.fromString( s, ',' ).flatMap( {entry : String =>
       val trimmedEntry = entry.trim()
       if ( trimmedEntry.length > 0) {
         List( Symbol( trimmedEntry ) )
       }
       else Nil
    })
  }

  /**
   * Converts a string with key-value pairs to a Map, where the keys and values are separated by =, and entries are separated by commas.
   */
  def stringToMap( keyValueString : String ) : Map[Symbol, String] = stringToMap(keyValueString, "=", ",")


  /**
   * Converts a string with key-value pairs to a Map.
   */
  def stringToMap( keyValueString : String, keyValueSeparator : String, entrySeparator : String ) : Map[Symbol, String] = {
    var properties : Map[Symbol, String] = Map()

    val rows = keyValueString.split( Pattern.quote( entrySeparator ) ).toList

    rows foreach { row : String =>

      val splitIndex = row.indexOf( keyValueSeparator )

      if ( splitIndex > 0 && splitIndex + 1 < row.length ) {

        val key = (row.substring(0, splitIndex)).trim
        val value = (row.substring(splitIndex + 1)).trim

        if (!key.isEmpty) {
          val entry = (Symbol(key), value)
          properties = properties + entry
        }
      }
    }

    properties
  }

  /**
   * Converts a map to a string with key-value pairs, where keys and values are separated by = and entries by comma. 
   * If the map has Symbol keys, the name of the symbol is used, not the toString method.
   */
  def mapToString( map : Map[Symbol, Any]) : String = mapToString(map, " = ", ", ")

  /**
   * Converts a map to a string with key-value pairs.
   * If the map has Symbol keys, the name of the symbol is used, not the toString method.
   */
  def mapToString( map : Map[Symbol, Any], keyValueSeparator : String, entrySeparator : String ) : String = {
    val s = new StringBuilder
    map foreach { case( key, value ) =>
      s.append( key.name )
      s.append( keyValueSeparator )
      s.append( value.toString )
      s.append( entrySeparator ) // TODO: Do not include for last entry
    }

    s.toString
  }



}
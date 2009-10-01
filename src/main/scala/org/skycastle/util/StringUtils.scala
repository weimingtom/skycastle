package org.skycastle.util

import _root_.scala.runtime.RichString
import java.nio.ByteBuffer

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
}
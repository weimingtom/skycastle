package org.skycastle.util

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
    new String( buffer.array, ENCODING_CHARSET )
  }

  /**
   * Encodes a {@code String} into a ByteBuffer, using the UTF-8 encoding.
   */
  def encodeString( s : String ) : ByteBuffer = {
    ByteBuffer.wrap( s.getBytes( ENCODING_CHARSET ) )
  }

}
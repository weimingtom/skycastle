package org.skycastle.network.protocol.binary


import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * Utility methods for dealing with packed numbers.
 * 
 * @author Hans Haggstrom
 */
// TODO: Implement the packing algorithm directly, instead of instantiating a BigInteger.
object PackedNumbers {

  private val MAX_NUMBER_OF_NUMBER_BYTES = 10

  /**
   * Calculates the number of bytes a given number will use when packed.
   */
  def length( value : Long ) : Int = {
    if (value > Math.MIN_BYTE + MAX_NUMBER_OF_NUMBER_BYTES && value <= Math.MAX_BYTE) {
      // The number fits in one byte, above the number-of-bytes indicator area
      1
    }
    else {
      val bytes =  BigInteger.valueOf( value ).toByteArray
      val numBytes = bytes.length
      if (numBytes > MAX_NUMBER_OF_NUMBER_BYTES) throw new IllegalStateException( "Problem when encoding packed number "+value+", way too big BigInteger representation." )
      else if (numBytes <= 0) throw new IllegalStateException( "Problem when encoding packed number "+value+", empty representation." )

      numBytes + 1 // The bytes used to store the number + indicator byte.
    }
  }

  /**
   * Encodes values between around -110 to 127 in one byte, and larger values in as many bytes as necessary + 1
   */
  def encode( buffer : ByteBuffer, value : Long ) {

    if (value > Math.MIN_BYTE + MAX_NUMBER_OF_NUMBER_BYTES && value <= Math.MAX_BYTE) {
      // The number fits in one byte, above the number-of-bytes indicator area
      buffer.put(value.toByte)
    }
    else {
      val bytes =  BigInteger.valueOf( value ).toByteArray
      val numBytes = bytes.length
      if (numBytes > MAX_NUMBER_OF_NUMBER_BYTES) throw new IllegalStateException( "Problem when encoding packed number "+value+", way too big BigInteger representation." )
      else if (numBytes <= 0) throw new IllegalStateException( "Problem when encoding packed number "+value+", empty representation." )

      // Encode number of bytes used near the negative lower range of a byte
      val indicatorByte : Byte = (Math.MIN_BYTE + numBytes).toByte
      buffer.put( indicatorByte )
      buffer.put( bytes )
    }
  }

  /**
   * Extracts an encoded packed number.
   */
  def decode( buffer : ByteBuffer ) : Long = {
    val indicatorByte : Byte = buffer.get

    if (indicatorByte > Math.MIN_BYTE + MAX_NUMBER_OF_NUMBER_BYTES) {
      // The number is small, was stored in the first byte
      indicatorByte.toLong
    }
    else {
      // Extract number of bytes in representation
      val numBytes = (indicatorByte.toInt) - Math.MIN_BYTE
      if (numBytes > MAX_NUMBER_OF_NUMBER_BYTES) throw new IllegalStateException( "Problem when decoding packed number, too many bytes in representation ("+numBytes+")." )
      else if (numBytes <= 0 ) throw new IllegalStateException( "Problem when decoding packed number, no bytes in representation." )

      // Read representation
      val bytes = new Array[Byte](numBytes)
      buffer.get( bytes )

      // Initialize to big integer, and get Long value
      new BigInteger( bytes ).longValue
    }
  }


}


package org.skycastle.protocol.binary

import entity.EntityId
import java.math.BigInteger
import java.nio.ByteBuffer
import util.Parameters
/**
 * List of types that can be encoded and decoded.
 */
/*
TODO: Implement serialization for these types:
  BYTE_BUFFER
  COLOR_4F
  COLOR_AWT
  VECTOR_3F
  QUATERNION
  MATRIX_3D
*/
class BinarySerializer {

  private val NULL_ID : Byte = 0
  private val OBJECT_ID_LEN = 1
  private val INT_LEN = 4
  private val CHAR_LEN = 2

  private val idToSerializer : Map[Byte, TypeSerializer[_]] = createIdToSerializerMap()

  def getSerializer( value : Any ) : Option[TypeSerializer[_]] = idToSerializer.values.find( _.canSerialize( value ) )

  def length(value: Any) : Int = {
    if (value == null) OBJECT_ID_LEN
    else getSerializer(value) match {
      case Some(serializer ) => OBJECT_ID_LEN + serializer.length( value )
      case None => OBJECT_ID_LEN
    }
  }

  def encode(buffer: ByteBuffer, value: Any ) {
    if (value == null) buffer.put( NULL_ID )
    else getSerializer(value) match {
      case Some(serializer ) => {
        buffer.put( serializer.id )
        serializer.encode( buffer, value )
      }
      case None => {
        ProtocolLogger.logWarning( "When encoding message: No encoder found for object '"+value+"', substituting with null." )
        buffer.put( NULL_ID )
      }
    }
  }

  def decode[T](buffer: ByteBuffer) : T = {
    val valueType = buffer.get
    if (valueType == NULL_ID) null.asInstanceOf[T]
    else idToSerializer.get(valueType) match {
      case Some(serializer ) => serializer.decode( buffer )
      case None => {
        ProtocolLogger.logInfo( "When decoding message: Unknown object type '"+valueType+"', substituting with null." )
        null.asInstanceOf[T]
      }
    }
  }

  private def createIdToSerializerMap() = {

    var types : Map[Byte, TypeSerializer[_]] = Map()

    var nextId  : Byte = 1
    def add( t : TypeSerializer[_]) {
      t.id = nextId
      nextId = (nextId + 1).byteValue

      if (nextId > 120 ) throw new IllegalStateException( "The number of supported types is growing too large for one byte.  Refactor protocol code." )

      val entry = ( t.id, t )
      types = types + entry
    }

    // NOTE: The order of these additions is important, as that is the order in which type ID:s are defined.  Do not change it!

    val anySerializer = this


    // Wrapped primitive types:

    add( new TypeSerializer( classOf[java.lang.Boolean] ) {
      def len(value: T) = 1
      def enc(buffer: ByteBuffer, value: T) = buffer.put( (if(value.booleanValue) 1 else 0).byteValue )
      def dec(buffer: ByteBuffer) = java.lang.Boolean.valueOf( buffer.get != 0 )
    })

    add( new TypeSerializer( classOf[java.lang.Byte] ) {
      def len(value: T) = 1
      def enc(buffer: ByteBuffer, value: T) = buffer.put( value.byteValue )
      def dec(buffer: ByteBuffer) = java.lang.Byte.valueOf( buffer.get )
    })

    add( new TypeSerializer( classOf[java.lang.Short] ) {
      def len(value: T) = lengthPackedNumber( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = encodePackedNumber( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Short.valueOf( decodePackedNumber( buffer ).shortValue )
    })
    

    add( new TypeSerializer( classOf[java.lang.Integer] ) {
      def len(value: T) = lengthPackedNumber( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = encodePackedNumber( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Integer.valueOf( decodePackedNumber( buffer ).intValue )
    })

    add( new TypeSerializer( classOf[java.lang.Long] ) {
      def len(value: T) = lengthPackedNumber( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = encodePackedNumber( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Long.valueOf( decodePackedNumber( buffer ) )
    })

    add( new TypeSerializer( classOf[java.lang.Float] ) {
      def len(value: T) = 4
      def enc(buffer: ByteBuffer, value: T) = buffer.putFloat( value.floatValue )
      def dec(buffer: ByteBuffer) = java.lang.Float.valueOf( buffer.getFloat )
    })

    add( new TypeSerializer( classOf[java.lang.Double] ) {
      def len(value: T) = 8
      def enc(buffer: ByteBuffer, value: T) = buffer.putDouble( value.doubleValue )
      def dec(buffer: ByteBuffer) = java.lang.Double.valueOf( buffer.getDouble )
    })

    add( new TypeSerializer( classOf[java.lang.Character] ) {
      def len(value: T) = 2
      def enc(buffer: ByteBuffer, value: T) = buffer.putChar( value.charValue )
      def dec(buffer: ByteBuffer) = java.lang.Character.valueOf( buffer.getChar )
    })


    // String:

    val stringSerializer : TypeSerializer[String] = new TypeSerializer[String]( classOf[String] ) {
      def len(value: T) = INT_LEN + value.getBytes.length // NOTE: Not so efficient way to get the length, but probably the most accurate - the string encoding can vary.
      def enc(buffer: ByteBuffer, value: T) {
        buffer.putInt( value.length )
        if (value.length > 0) buffer.put( value.getBytes )
      }
      def dec(buffer: ByteBuffer) : T = {
        val length = buffer.getInt()
        if (length <= 0) ""
        else {
          val s = new Array[Byte](length)
          buffer.get( s )
          new String(s)
        }
      }
    }
    add( stringSerializer )

    // ID types:

    val symbolSerializer = new TypeSerializer( classOf[Symbol] ) {
      def len(value: T) = anySerializer.length( value.name )
      def enc(buffer: ByteBuffer, value: T) = anySerializer.encode( buffer, value.name )
      def dec(buffer: ByteBuffer) = Symbol( anySerializer.decode( buffer ) )
    }
    add( symbolSerializer )

    add( new TypeSerializer( classOf[EntityId] ) {
      def len(value: T) = anySerializer.length( value.id )
      def enc(buffer: ByteBuffer, value: T) = anySerializer.encode( buffer, value.id )
      def dec(buffer: ByteBuffer) = EntityId( anySerializer.decode( buffer ) )
    })
    
    // Collections:

    add( new TypeSerializer[List[Any]]( classOf[List[Any]] ) {
      def len(value: T) = INT_LEN + lenCollection( value )
      def enc(buffer: ByteBuffer, value: T) {
        buffer.putInt( value.size )
        value foreach { x => anySerializer.encode( buffer, x ) }
      }
      def dec(buffer: ByteBuffer) = {
        var numEntries = buffer.getInt()
        var resultList : T = Nil
        while (numEntries > 0) {
          val value = anySerializer.decode[Any](buffer)
          resultList = resultList ::: List(value)

          numEntries -= 1
        }

        resultList
      }
    })

    add( new TypeSerializer[Set[Any]]( classOf[Set[Any]] ) {
      def len(value: T) = INT_LEN + lenCollection( value )
      def enc(buffer: ByteBuffer, value: T) {
        buffer.putInt( value.size )
        value foreach { x => anySerializer.encode( buffer, x ) }
      }
      def dec(buffer: ByteBuffer) = {
        var numEntries = buffer.getInt()
        var resultSet : T = Set()
        while (numEntries > 0) {
          val value = anySerializer.decode[Any]( buffer )
          resultSet = resultSet + value

          numEntries -= 1
        }

        resultSet
      }
    })

    add( new TypeSerializer[Map[Any,Any]]( classOf[Map[Any,Any]] ) {
      def len(value: T) = INT_LEN + lenIterator( value.keys ) + lenIterator( value.values )
      def enc(buffer: ByteBuffer, value: T) {
        buffer.putInt( value.size )
        value foreach {case (key, value) =>
          anySerializer.encode( buffer, key )
          anySerializer.encode( buffer, value )
        }
      }
      def dec(buffer: ByteBuffer) = {
        var numEntries = buffer.getInt()
        var resultMap : T = Map()
        while (numEntries > 0) {
          val key = anySerializer.decode[Any]( buffer )
          val value = anySerializer.decode[Any]( buffer )
          val entry = (key, value)
          resultMap = resultMap + entry

          numEntries -= 1
        }

        resultMap
      }
    })

    add( new TypeSerializer[Parameters]( classOf[Parameters] ) {
      def len(value: T) = {
        INT_LEN +
         lenIterator( value.entries.keys ) +
         lenIterator( value.entries.values ) -
         OBJECT_ID_LEN * value.entries.size // We know that all keys are Symbols, so that need not be stored
      }
      def enc(buffer: ByteBuffer, value: T) {
        buffer.putInt( value.entries.size )
        value.entries foreach {case (key, value) =>
          symbolSerializer.encode( buffer, key )
          anySerializer.encode( buffer, value )
        }
      }
      def dec(buffer: ByteBuffer) = {
        var numEntries = buffer.getInt()
        var resultMap : Map[Symbol,Any] = Map()
        while (numEntries > 0) {
          val key = symbolSerializer.decode( buffer )
          val value = anySerializer.decode[Any]( buffer )
          val entry = (key, value)
          resultMap = resultMap + entry

          numEntries -= 1
        }

        new Parameters( resultMap )
      }
    })


    // Primitive types:
    // NOTE: All primitive types seem to get wrapped up in boxed types at the moment, so we can leave the primitive encoders away.
/*
    add( new TypeSerializer[Boolean]( classOf[Boolean] ) {
      def len(value: T) = 1
      def enc(buffer: ByteBuffer, value: T) = buffer.put( (if(value) 1 else 0).byteValue )
      def dec(buffer: ByteBuffer) = buffer.get != 0
    })

    add( new TypeSerializer( classOf[Byte] ) {
      def len(value: T) = 1
      def enc(buffer: ByteBuffer, value: T) = buffer.put( value )
      def dec(buffer: ByteBuffer) = buffer.get
    })

    add( new TypeSerializer( classOf[Short] ) {
      def len(value: T) = 2
      def enc(buffer: ByteBuffer, value: T) = buffer.putShort( value )
      def dec(buffer: ByteBuffer) = buffer.getShort
    })

    add( new TypeSerializer( classOf[Int] ) {
      def len(value: T) = 4
      def enc(buffer: ByteBuffer, value: T) = buffer.putInt( value )
      def dec(buffer: ByteBuffer) = buffer.getInt
    })

    add( new TypeSerializer( classOf[Long] ) {
      def len(value: T) = 8
      def enc(buffer: ByteBuffer, value: T) = buffer.putLong( value )
      def dec(buffer: ByteBuffer) = buffer.getLong
    })

    add( new TypeSerializer( classOf[Float] ) {
      def len(value: T) = 4
      def enc(buffer: ByteBuffer, value: T) = buffer.putFloat( value )
      def dec(buffer: ByteBuffer) = buffer.getFloat
    })

    add( new TypeSerializer( classOf[Double] ) {
      def len(value: T) = 8
      def enc(buffer: ByteBuffer, value: T) = buffer.putDouble( value )
      def dec(buffer: ByteBuffer) = buffer.getDouble
    })

    add( new TypeSerializer( classOf[Char] ) {
      def len(value: T) = 2
      def enc(buffer: ByteBuffer, value: T) = buffer.putChar( value )
      def dec(buffer: ByteBuffer) = buffer.getChar
    })
*/


    types
  }

  /**
   * Utility function that calculates length in bytes of a collection of objects.
   */
  private def lenCollection( collection : Collection[Any] ) : Int = collection.foldLeft( 0 ){ _ + length( _ ) }

  /**
   * Utility function that calculates length in bytes of an iterator over objects.
   */
  private def lenIterator( collection : Iterator[Any] )     : Int = collection.foldLeft( 0 ){ _ + length( _ ) }


  val MAX_NUMBER_OF_NUMBER_BYTES = 10


  // TODO: Use the packing algorithm directly, instead of instantiating a BigInteger.
  private def lengthPackedNumber( value : Long ) : Int = {
    if (value > Math.MIN_BYTE + MAX_NUMBER_OF_NUMBER_BYTES && value <= Math.MAX_BYTE) {
      // The number fits in one byte, above the number-of-bytes indicator area
      1
    }
    else {
      val bytes =  BigInteger.valueOf( value ).toByteArray
      val numBytes = bytes.length
      if (numBytes > MAX_NUMBER_OF_NUMBER_BYTES) throw new IllegalStateException( "Problem when encoding packed number "+value+", way too big BigInteger representation." )
      else if (numBytes <= 0) throw new IllegalStateException( "Problem when encoding packed number "+value+", empty representation." )

      numBytes + 1 // Indicator byte + the bytes used to store the number.
    }
  }

  /**
   * Encodes values between around -110 to 127 in one byte, and larger values in as many bytes as necessary + 1
   */
  // TODO: Use the packing algorithm directly, instead of instantiating a BigInteger.
  private def encodePackedNumber( buffer : ByteBuffer, value : Long ) {

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
  // TODO: Use the packing algorithm directly, instead of instantiating a BigInteger.
  private def decodePackedNumber( buffer : ByteBuffer ) : Long = {
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
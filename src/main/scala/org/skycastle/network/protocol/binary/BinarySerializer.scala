package org.skycastle.network.protocol.binary

import entity.EntityId
import java.lang.reflect.{Modifier, Method}
import java.math.BigInteger
import java.nio.ByteBuffer
import util.Parameters
/**
 * Takes care of serializing and deserializing a set of allowed types from byte buffers.
 * @author Hans Haggstrom
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
// TODO: Maybe refactor to separate the functions of encodign and decodign objects and defining the serializers.
@serializable
@SerialVersionUID(1)
class BinarySerializer( hostObjectId : EntityId ) {

  private val NULL_ID : Byte = 0
  private val OBJECT_ID_LEN = 1
  private val INT_LEN = 4
  private val CHAR_LEN = 2

  private val idToSerializer : Map[Byte, TypeSerializer[_]] = createSerializers()

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

  private def createSerializers() = {

    var types : Map[Byte, TypeSerializer[_]] = Map()

    var nextId  : Byte = 1
    def add( t : TypeSerializer[_]) {
      t.id = nextId
      nextId = (nextId + 1).byteValue

      if (nextId > 120 ) throw new IllegalStateException( "The number of supported types is growing too large for one byte.  Refactor protocol code." )

      val entry = ( t.id, t )
      types = types + entry
    }

    val anySerializer = this

    // NOTE: The order of these additions is important, as that is the order in which type ID:s are defined.
    // Do not change it!

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
      def len(value: T) = PackedNumbers.length( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = PackedNumbers.encode( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Short.valueOf( PackedNumbers.decode( buffer ).shortValue )
    })
    

    add( new TypeSerializer( classOf[java.lang.Integer] ) {
      def len(value: T) = PackedNumbers.length( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = PackedNumbers.encode( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Integer.valueOf( PackedNumbers.decode( buffer ).intValue )
    })

    add( new TypeSerializer( classOf[java.lang.Long] ) {
      def len(value: T) = PackedNumbers.length( value.longValue )
      def enc(buffer: ByteBuffer, value: T) = PackedNumbers.encode( buffer, value.longValue )
      def dec(buffer: ByteBuffer) = java.lang.Long.valueOf( PackedNumbers.decode( buffer ) )
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
      def len(value: T) = {
        val encodedPath : List[String]= value.encode( hostObjectId )
        anySerializer.length( encodedPath  )
      }
      def enc(buffer: ByteBuffer, value: T) = {
        val encodedPath : List[String]= value.encode( hostObjectId )
        anySerializer.encode( buffer, encodedPath )
      }
      def dec(buffer: ByteBuffer) = {
        val path : List[String] = anySerializer.decode( buffer )
        EntityId.decode( hostObjectId, path )
      }
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


    // Arbitrary types that implement own serialization and deserialization
    add( new TypeSerializer( classOf[Transferable] ) {
      def len(value: Transferable) = symbolSerializer.length( Symbol( value.decoderTypeName ) ) +
                                     anySerializer.length( value.toTransferObject )
      def enc(buffer: ByteBuffer, value: Transferable) = {
        symbolSerializer.encode( buffer, Symbol( value.decoderTypeName ) )
        anySerializer.encode( buffer, value.toTransferObject )
      }

      def dec(buffer: ByteBuffer) : Transferable =  {
        val typeName = symbolSerializer.decode( buffer ).name

        try {
          val typeClass : Class[_]= Class.forName( typeName )
          if ( classOf[TransferableDecoder].isAssignableFrom( typeClass ) ) {
            try {
              val factoryMethod : Method = typeClass.getMethod( "fromTransferObject", classOf[Object] )

              if ( Modifier.isStatic( factoryMethod.getModifiers ) ) {
                val valueObject = anySerializer.decode( buffer )
                factoryMethod.invoke( null, valueObject ).asInstanceOf[Transferable]
              }
              else {
                ProtocolLogger.logWarning( "When decoding message: The specified decode method for transferable type '"+typeName+"' is not static.  Substituting with null." )
                null.asInstanceOf[Transferable]
              }
            }
            catch {
              case e  =>
                ProtocolLogger.logInfo( "When decoding message: Could not instantiate transferable type '"+typeName+"', substituting with null.  Error: " + e.getMessage, e )
                null.asInstanceOf[Transferable]
            }
          }
          else {
            ProtocolLogger.logWarning( "When decoding message: Can not decode to transferable type '"+typeName+"', it doesn't implement Transferable.  Substituting with null." )
            null.asInstanceOf[Transferable]
          }
        }
        catch {
          case e : ClassNotFoundException =>
            ProtocolLogger.logInfo( "When decoding message: Unknown transferable type '"+typeName+"', substituting with null.", e )
            null.asInstanceOf[Transferable]
        }
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



}
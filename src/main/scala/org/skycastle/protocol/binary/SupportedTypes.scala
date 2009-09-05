package org.skycastle.protocol.binary

import java.nio.ByteBuffer
import types._
/**
 * 
 */
object SupportedTypes {

  val numberToType : Map[Byte, SerializableType] = createNumberToType()
  val classToType : Map[Class[_], SerializableType] = createClassToType( numberToType )

  val OBJECT_TYPE_LEN = 1

  def encodeObject( buffer: ByteBuffer, value: Object ) {
    if (value == null) buffer.put( ProtocolConstants.NULL )
    else {
      val kind = value.getClass
      classToType.get( kind ) match {
        case Some( encoder : SerializableType ) => {
          buffer.put( encoder.number )
          encoder.encode( buffer, value.asInstanceOf[encoder.T] )
        }
        case None => {
          ProtocolLogger.logWarning( "No encoder for object type '"+kind+"', substituting with null." )
          buffer.put( ProtocolConstants.NULL )
        }
      }
    }
  }

  def decodeObject( buffer: ByteBuffer ) : Object = {
    // Max nr of object types is 256.  If we need more, we can create an extension type.
    val objectType : Byte = buffer.get

    numberToType.get( objectType ) match  {
      case Some( decoder : SerializableType  ) => decoder.decode( buffer ).asInstanceOf[Object]
      case None => {
        ProtocolLogger.logInfo( "Unknown object type '"+objectType+"', substituting with null." )
        null
      }
    }
  }

  def objectLength( value : Object ) : Int = {
    if (value == null) OBJECT_TYPE_LEN
    else {
      val kind = value.getClass
      classToType.get( kind ) match {
        case Some( lengthCalculator : SerializableType ) => OBJECT_TYPE_LEN + lengthCalculator.length( value.asInstanceOf[lengthCalculator.T] )
        case None => OBJECT_TYPE_LEN
      }
    }
  }


  def lenCollection( collection : Collection[Object] ) : Int = collection.foldLeft( 0 ){ _ + objectLength( _ ) }
  def lenIterator( collection : Iterator[Object] )     : Int = collection.foldLeft( 0 ){ _ + objectLength( _ ) }


  private final def createNumberToType() = {

    var types : Map[Byte, SerializableType] = Map()

    def addType( t : SerializableType ) {
      val entry = ( t.number, t )
      types = types + entry
    }

    addType( BooleanType )

    addType( ByteType )
    addType( ShortType )
    addType( IntType )
    addType( LongType )

    addType( FloatType )
    addType( DoubleType )

    addType( CharType )
    addType( StringType )

    addType( SymbolType )
    addType( EntityIdType )

    addType( ListType )
    addType( SetType )
    addType( MapType )
    
    addType( ParametersType )


    types
  }

  private def createClassToType( numberToType : Map[Byte, SerializableType] ) : Map[Class[_], SerializableType] = {
    var types : Map[Class[_], SerializableType] = Map()

    def addType( t : SerializableType ) {
      val entry = ( t.kind, t )
      types = types + entry
    }

    numberToType.values foreach addType
    
    types
  }

}
package org.skycastle.protocol.binary.types


import java.nio.ByteBuffer

/**
 * This delegates to another type using the SupportedTypes.
 * 
 * @author Hans Haggstrom
 */
object ObjectType extends SerializableType  {

  val OBJECT_TYPE_LEN = 1

  type T = Object
  val number : Byte = -1
  val kind = classOf[T]

  def encode( buffer: ByteBuffer, value: T ) {
    if (value == null) buffer.put( ProtocolConstants.NULL )
    else {
      val kind = value.getClass
      SupportedTypes.classToType.get( kind ) match {
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

  def decode( buffer: ByteBuffer ) : T = {
    // Max nr of object types is 256.  If we need more, we can create an extension type.
    val objectType : Byte = buffer.get

    if (objectType == ProtocolConstants.NULL) null
    else SupportedTypes.numberToType.get( objectType ) match  {
      case Some( decoder : SerializableType  ) => decoder.decode( buffer ).asInstanceOf[Object]
      case None => {
        ProtocolLogger.logInfo( "Unknown object type '"+objectType+"', substituting with null." )
        null
      }
    }
  }

  def length( value : T ) : Int = {
    if (value == null) OBJECT_TYPE_LEN
    else {
      val kind = value.getClass
      SupportedTypes.classToType.get( kind ) match {
        case Some( lengthCalculator : SerializableType ) => OBJECT_TYPE_LEN + lengthCalculator.length( value.asInstanceOf[lengthCalculator.T] )
        case None => OBJECT_TYPE_LEN
      }
    }
  }

}


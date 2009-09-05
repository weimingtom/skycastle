package org.skycastle.protocol.binary

import java.nio.ByteBuffer
import types._
/**
 * List of types that can be encoded and decoded.
 */
/*
TODO: Implement these types
  BYTE_BUFFER 16
  COLOR_4F    17
  COLOR_AWT   18
  VECTOR_3F   19
  QUATERNION  20
  MATRIX_3D   21
*/
object SupportedTypes {

  val numberToType : Map[Byte, SerializableType] = createNumberToType()
  val classToType : Map[Class[_], SerializableType] = createClassToType( numberToType )


  private def createNumberToType() = {

    var types : Map[Byte, SerializableType] = Map()

    def addType( t : SerializableType ) {
      val entry = ( t.number, t )
      types = types + entry
    }

    // NOTE: ObjectType not added, it can be called directly
    
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
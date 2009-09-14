package org.skycastle.network.protocol.binary

import _root_.junit.framework.TestCase
import entity.EntityId
import java.nio.ByteBuffer
import org.junit._
import Assert._
import util.Parameters

/**
 * Test binary serializers.
 * 
 * @author Hans Haggstrom
 */
@Test
class BinaryProtocolTest extends TestCase {

  var protocol : BinaryProtocol = null
  var serializer : BinarySerializer = null
  var bridgeId : EntityId = null

  override def setUp = {
    bridgeId = EntityId( "unusedId70643" )
    protocol = new BinaryProtocol()
    serializer = new BinarySerializer( bridgeId )
    protocol.init( bridgeId )
  }

  @Test
  def testEntityIdCoding {
    val clientBridgeId = EntityId( "clientBridge" )
    val serverBridgeId = EntityId( "serverBridge" )
    val clientObjectId = EntityId( "clientObject" )

    assertEquals( List( "clientObject" ), clientObjectId.path )
    val code = clientObjectId.encode( clientBridgeId )
    assertEquals( List( "0", "clientObject" ), code )
    val serverSideObjectId = EntityId.decode( serverBridgeId, code )
    assertEquals( List( "serverBridge", "clientObject" ), serverSideObjectId.path )

    val code2 = serverSideObjectId.encode( serverBridgeId )
    assertEquals( List( "0", "clientObject" ), code2 )
    val clientObjectBackAtClient = EntityId.decode( clientBridgeId, code2 )
    assertEquals( List( "clientObject" ), clientObjectBackAtClient.path )
  }

  @Test
  def testMessages {

    val order = Message( EntityId( "entity42" ), 'bake,
                         Parameters(
                           'receipt -> "pizza",
                           'size -> 20,
                           'extraSauce -> true,
                           'otherAdditions -> List( "Mustard", "Onions", "Pineapple" ) ) )

    val buffer = protocol.encode( order )
    val mangledOrder = protocol.decode( buffer ).head
    println( mangledOrder )
    val buffer2 = protocol.encode( mangledOrder )
    val mangledOrder2 = protocol.decode( buffer2 ).head

    assertEquals( "The message should have passed correctly through the BinaryProtocol",
                  order, mangledOrder2 )
  }

  @Test
  def testTypes {

    asserSerializes( null)

    asserSerializes( true )
    asserSerializes( false )

    asserSerializes( (-128).byteValue )
    asserSerializes( (-1).byteValue )
    asserSerializes( 0.byteValue )
    asserSerializes( 23.byteValue )
    asserSerializes( 127.byteValue )
    asserSerializes( Math.MAX_BYTE )
    asserSerializes( Math.MIN_BYTE )

    asserSerializes( 0.shortValue )
    asserSerializes( 893.shortValue )
    asserSerializes( (-2833).shortValue )
    asserSerializes( Math.MAX_SHORT )
    asserSerializes( Math.MIN_SHORT )

    asserSerializes( 4 )
    asserSerializes( -3 )
    asserSerializes( 0 )
    asserSerializes( 238218 )
    asserSerializes( -90393 )
    asserSerializes( Math.MAX_INT )
    asserSerializes( Math.MIN_INT )

    asserSerializes( 0L )
    asserSerializes( 8L )
    asserSerializes( 9853L )
    asserSerializes( 58425943L )
    asserSerializes( -59405L )
    asserSerializes( 2313234344235L )
    asserSerializes( Math.MAX_LONG )
    asserSerializes( Math.MIN_LONG )

    asserSerializes( 0.0f )
    asserSerializes( 123.0021f )
    asserSerializes( -0.000123f )
    asserSerializes( 122.0e8f )
    asserSerializes( 32.0e-7f )
    asserSerializes( Math.NaN_FLOAT )
    asserSerializes( Math.POS_INF_FLOAT )
    asserSerializes( Math.NEG_INF_FLOAT )

    asserSerializes( 0.0 )
    asserSerializes( 123243.002321 )
    asserSerializes( -0.000000123 )
    asserSerializes( 53.0e22 )
    asserSerializes( -32.0e-17 )
    asserSerializes( Math.NaN_DOUBLE)
    asserSerializes( Math.POS_INF_DOUBLE )
    asserSerializes( Math.NEG_INF_DOUBLE )

    asserSerializes( ' ')
    asserSerializes( 'a')
    asserSerializes( 'ä')
    asserSerializes( '@')
    asserSerializes( '\n')

    asserSerializes( "")
    asserSerializes( " " )
    asserSerializes( "Foobar foo bar")
    asserSerializes( "AAAAaaAAAAAAAAaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaAAAAAAAAAAAA!!!!!!!1111111111")

    asserSerializes( 'foobar )
    asserSerializes( 'a )
    asserSerializes( Symbol("") )
    asserSerializes( Symbol(null) )
    asserSerializes( Symbol("foo bar") )

/* TODO: Test with double serialization?
    asserSerializes( EntityId("foobar") )
    asserSerializes( EntityId("foo bar") )
    asserSerializes( EntityId("") )
    asserSerializes( EntityId(" ") )
*/

    asserSerializes( Nil)
    asserSerializes( List( null ))
    asserSerializes( List( 10, "a", 'Hello ))
    asserSerializes( List( 1, List( 2, List (3)) ))

    asserSerializes( Set(  ))
    asserSerializes( Set( 'one ))
    asserSerializes( Set( 1, 2, 3, 4 ))
    asserSerializes( Set( 1, 2, 3, 3, "4" ))

    asserSerializes( Map( ))
    asserSerializes( Map( 0 -> 1 ))
    asserSerializes( Map( 'a -> "*B*", 'c' -> 0xd, "foobar bar" -> null, 0 -> null))

    asserSerializes( Parameters())
    asserSerializes( Parameters( 'foo -> 1 ) )
    asserSerializes( Parameters( 'foo -> true, 'bar -> 32312L, 'zap -> 0.33f, 'zoo -> Math.POS_INF_DOUBLE, 'zapap -> Map( "a" -> 1.toShort, "b" ->(-1).toByte ) ) )

  }

  def asserSerializes[T]( value : T ) {
    def typeName( value : Any ) = if (value == null) "null" else value.asInstanceOf[Object].getClass.getSimpleName

    val length_bytes = serializer.length( value )

    val buffer = ByteBuffer.wrap( new Array[Byte]( length_bytes ) )

/*
    println( "Serializing "+typeName(value )+" " + value )
    println( "Length is " + length_bytes + " bytes" )
*/

    serializer.encode( buffer, value )

    buffer.flip()

/*
    println( " = bytes " + buffer.array().mkString( " " ) )
    println( " = hex   " + buffer.array().map( {b :Byte => String.format("%02X", java.lang.Byte.valueOf( b )) } ).mkString( " " ) )
*/

    val serializedValue = serializer.decode[T]( buffer )

/*
    if (serializedValue == null) println ("Decoded to null\n")
    else println( "Decoded to  "+typeName(serializedValue )+" " + serializedValue + "\n")
*/

    assertEquals( "The serializer for " + typeName(value )+" should encode and deoced the value correctly.",
                  value, serializedValue)
  }

}


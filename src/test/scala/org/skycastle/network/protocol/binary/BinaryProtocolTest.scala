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
    assertEquals( List( "clientObject" ), code2 )
    val clientObjectBackAtClient = EntityId.decode( clientBridgeId, code2 )
    assertEquals( List( "clientObject" ), clientObjectBackAtClient.path )
  }

  @Test
  def testMessages {

    val order = Message( bridgeId, 
                         EntityId( "entity42" ),
                         'bake,
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

    assertSerializes( null)

    assertSerializes( true )
    assertSerializes( false )

    assertSerializes( (-128).byteValue )
    assertSerializes( (-1).byteValue )
    assertSerializes( 0.byteValue )
    assertSerializes( 23.byteValue )
    assertSerializes( 127.byteValue )
    assertSerializes( Math.MAX_BYTE )
    assertSerializes( Math.MIN_BYTE )

    assertSerializes( 0.shortValue )
    assertSerializes( 893.shortValue )
    assertSerializes( (-2833).shortValue )
    assertSerializes( Math.MAX_SHORT )
    assertSerializes( Math.MIN_SHORT )

    assertSerializes( 4 )
    assertSerializes( -3 )
    assertSerializes( 0 )
    assertSerializes( 238218 )
    assertSerializes( -90393 )
    assertSerializes( Math.MAX_INT )
    assertSerializes( Math.MIN_INT )

    assertSerializes( 0L )
    assertSerializes( 8L )
    assertSerializes( 9853L )
    assertSerializes( 58425943L )
    assertSerializes( -59405L )
    assertSerializes( 2313234344235L )
    assertSerializes( Math.MAX_LONG )
    assertSerializes( Math.MIN_LONG )

    assertSerializes( 0.0f )
    assertSerializes( 123.0021f )
    assertSerializes( -0.000123f )
    assertSerializes( 122.0e8f )
    assertSerializes( 32.0e-7f )
    assertSerializes( Math.NaN_FLOAT )
    assertSerializes( Math.POS_INF_FLOAT )
    assertSerializes( Math.NEG_INF_FLOAT )

    assertSerializes( 0.0 )
    assertSerializes( 123243.002321 )
    assertSerializes( -0.000000123 )
    assertSerializes( 53.0e22 )
    assertSerializes( -32.0e-17 )
    assertSerializes( Math.NaN_DOUBLE)
    assertSerializes( Math.POS_INF_DOUBLE )
    assertSerializes( Math.NEG_INF_DOUBLE )

    assertSerializes( ' ')
    assertSerializes( 'a')
    assertSerializes( 'ä')
    assertSerializes( '@')
    assertSerializes( '\n')

    assertSerializes( "")
    assertSerializes( " " )
    assertSerializes( "Foobar foo bar")
    assertSerializes( "AAAAaaAAAAAAAAaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaAAAAAAAAAAAA!!!!!!!1111111111")

    assertSerializes( 'foobar )
    assertSerializes( 'a )
    assertSerializes( Symbol("") )
    assertSerializes( Symbol(null) )
    assertSerializes( Symbol("foo bar") )

/* TODO: Test with double serialization?
    assertSerializes( EntityId("foobar") )
    assertSerializes( EntityId("foo bar") )
    assertSerializes( EntityId("") )
    assertSerializes( EntityId(" ") )
*/

    assertSerializes( Nil)
    assertSerializes( List( null ))
    assertSerializes( List( 10, "a", 'Hello ))
    assertSerializes( List( 1, List( 2, List (3)) ))

    assertSerializes( Set(  ))
    assertSerializes( Set( 'one ))
    assertSerializes( Set( 1, 2, 3, 4 ))
    assertSerializes( Set( 1, 2, 3, 3, "4" ))

    assertSerializes( Map( ))
    assertSerializes( Map( 0 -> 1 ))
    assertSerializes( Map( 'a -> "*B*", 'c' -> 0xd, "foobar bar" -> null, 0 -> null))

    assertSerializes( Parameters())
    assertSerializes( Parameters( 'foo -> 1 ) )
    assertSerializes( Parameters( 'foo -> true, 'bar -> 32312L, 'zap -> 0.33f, 'zoo -> Math.POS_INF_DOUBLE, 'zapap -> Map( "a" -> 1.toShort, "b" ->(-1).toByte ) ) )

    assertSerializes( DummyTransferable( "Yarr", 42, 3.1415f ) )

  }

  def assertSerializes[T]( value : T ) {
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



object DummyTransferable {

  def fromTransferObject( obj : Object ) : DummyTransferable = {
    val params = value.asInstanceOf[Parameters]

    DummyTransferable( params.getString( 'foo, null ),
                       params.getInt( 'bar, 0),
                       params.getFloat( 'zap, 0f ) )

  }

}

case class DummyTransferable(foo : String, bar : Int, zap : Float) extends Transferable {

  def toTransferObject = Parameters( 'foo -> foo, 'bar -> bar, 'zap -> zap )
}



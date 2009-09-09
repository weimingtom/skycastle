package org.skycastle.util


import _root_.junit.framework.TestCase
import org.junit._
import Assert._

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@Test
class ParametersTest extends TestCase {

  @Test
  def testProperties {

    val prop = new Properties( 'foo -> 'baa, 'bar -> 12, 'd -> 3.15, 'zap -> "meh", 'ffff -> List( 1, 'two, "three", 4.0, new Integer(5) ) )

    assertEquals( Some(12), prop.get( 'bar ) )
    assertEquals( 'baa, prop.getAs[Symbol]( 'foo, 'meh ) )
    assertEquals( 3.15, prop.getDouble( 'd, 10.0 ), 0.001 )
    assertEquals( 10.0, prop.getDouble( 'e, 10.0 ), 0.001 )
    assertEquals( "12", prop.getAsString( 'bar, "" ) )
    assertEquals( true, prop.has( 'bar ) )
    assertEquals( false, prop.has( 'barbar ) )
    
    prop.set( 'foo, "sheep" )
    assertEquals( "sheep", prop.get( 'foo, null ) )

  }


}
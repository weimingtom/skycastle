package org.skycastle.util


import _root_.junit.framework.TestCase
import org.junit._
import Assert._
import org.scalatest.Suite

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class ParametersTest extends Suite {

  def testProperties {

    val prop = new SimpleProperties( 'foo -> 'baa, 'bar -> 12, 'd -> 3.15, 'zap -> "meh", 'ffff -> List( 1, 'two, "three", 4.0, new java.lang.Integer(5) ) )

    assertEquals( Some(12), prop.getProperty( 'bar ) )
    assertEquals( 'baa, prop.getProperty( 'foo, 'meh ) )
    assertEquals( 3.15, prop.getDouble( 'd, 10.0 ), 0.001 )
    assertEquals( 10.0, prop.getDouble( 'e, 10.0 ), 0.001 )
    assertEquals( "12", prop.getAsString( 'bar, "" ) )
    assertEquals( true, prop.hasProperty( 'bar ) )
    assertEquals( false, prop.hasProperty( 'barbar ) )
    
    prop.setProperty( 'foo, "sheep" )
    assertEquals( "sheep", prop.getProperty( 'foo, null ) )

  }


}
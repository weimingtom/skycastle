package org.skycastle.util


import _root_.junit.framework.TestCase
import org.junit._
import Assert._
import StringUtils._

/**
 * 
 */
@Test
class StringUtilsTest extends TestCase {

  @Test
  def testIsIdentifier {

    assertTrue( isIdentifier( "foobar" ) )
    assertTrue( isIdentifier( "F1234" ) )
    assertTrue( isIdentifier( "$" ) )
    assertTrue( isIdentifier( "a" ) )
    assertTrue( isIdentifier( "a2" ) )
    assertTrue( isIdentifier( "öåä" ) )
    assertTrue( isIdentifier( "_" ) )
    assertTrue( isIdentifier( "______" ) )

    assertFalse( isIdentifier( "1" ) )
    assertFalse( isIdentifier( "#e" ) )
    assertFalse( isIdentifier( ".2" ) )
    assertFalse( isIdentifier( "" ) )
    assertFalse( isIdentifier( null ) )
    assertFalse( isIdentifier( "@asd" ) )
    assertFalse( isIdentifier( "3gdfg" ) )

  }

}
package org.skycastle.util

import _root_.junit.framework.TestCase
import org.junit._
import Assert._
import StringUtils._

/**
 *
 */
@Test
class PropertyTest extends TestCase {

  class Dummy {
    val name = Property( "Igor", Property.notNull )
    val force = Property( 1.5f )
    val injuries = Property[List[String]]( List("Thumb Trauma", "Sore Eye"), Property.notNull )
  }

  var dummy : Dummy = null
  override def setUp = {
    dummy = new Dummy
  }

  @Test
  def testProperty {

    def f( s : String ) = s
    def g( v : Float ) = v

    assertEquals( "Igor", f( dummy.name ) )
    assertEquals( 1.5f, g( dummy.force ) )
    assertEquals( List("Thumb Trauma", "Sore Eye"), dummy.injuries.value )

    dummy.force := 20f
    dummy.name.set( "Hurgan" )

    assertEquals( 20f, dummy.force.value )
    assertEquals( "Hurgan", f( dummy.name ) )

    assertThrowsException{
      dummy.name := null
    }
  }

  def assertThrowsException( code : => Unit ) {

    try {
      code
      fail("Should throw exception")
    }
    catch {
      case _ =>
    }
  }

  
}
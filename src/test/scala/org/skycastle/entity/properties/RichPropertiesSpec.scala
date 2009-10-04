package org.skycastle.entity.properties

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, Spec, Suite}
import org.skycastle.entity.properties.PropertyConversions._
import org.skycastle.entity.accesscontrol.Role

/**
 * Specification / test for RichProperties 
 */
class RichPropertiesSpec extends Spec with ShouldMatchers with BeforeAndAfter {

  class Orc() extends RichProperties {
    'name :- "Igor"
    'mana :- 50
    val hitpoints = 'hitpoints :- 100 invariant {_ > 0}
  }
  var orc : Orc = null
  override protected def beforeEach() { orc = new Orc() }
  
  describe( "RichProperties" ) {

    it( "should cast properties to their value type as needed" ) {
      val hp : Int = orc.hitpoints
      hp should equal (100)
    }

    it( "should allow reading values of properties defined in a decendant class constructor" ) {
      orc('mana) should equal (50)
    }
    
    it( "should allow property change" ) {
      orc.hitpoints should equal (100)
      orc.hitpoints := 70
      orc.hitpoints should equal (70)
    }

    it( "should allow listening to property changes" ) {
      var reportedValue = 0
      orc.hitpoints onChange { x => reportedValue = x}

      orc.hitpoints := 50
      reportedValue should equal (50)
    }

    it( "should support invariants for properties" ) {
      intercept[java.lang.IllegalArgumentException] {
        orc.hitpoints := -10
      }
    }

    it( "should enforce type of property values" ) {
      intercept[java.lang.IllegalArgumentException] {
        orc('mana) := "walking upside down in the sky"
      }
    }

    it ("should allow adding new properties") {
      orc.addProperty( 'stamina, 80f )
      orc( 'stamina ) should be (80f)
    }

    it ("should allow adding new properties with shortcut notations") {
      orc :+ 'level :- 4
      orc :+ 'stamina :- 80 invariant { x : Int => x < orc.hitpoints.value }

      orc( 'level ) should be (4)
      orc ( 'stamina ) should be (80f)
    }

    it ("should have shorthand notation for accessing properties") {
      orc~'mana := 10
      orc~'mana should be (10)
    }

    it ("should allow specifying the type of a property") {
      orc :+ 'favouriteObject :/ classOf[Object] :- "abccb"
      (orc~'favouriteObject).kind should equal (classOf[Object])
    }

    it ("should not allow redefining properties") {
      intercept[java.lang.IllegalArgumentException] {
        orc.addProperty( 'hitpoints, 80f )
      }
    }

    it( "should support roles for access control" ) {
      val bossRole = Role( 'secretBoss )
      val agentRole = Role( 'secretAgent )

      orc :+ 'secretData :- "attack at dawn" editor bossRole reader agentRole

      bossRole.hasWriteCapability( 'secretData ) should be (true)
      bossRole.hasReadCapability( 'secretData ) should be (true)

      agentRole.hasWriteCapability( 'secretData ) should be (false)
      agentRole.hasReadCapability( 'secretData ) should be (true)
    }
    
  }



}



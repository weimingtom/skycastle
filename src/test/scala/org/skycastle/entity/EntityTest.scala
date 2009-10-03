package org.skycastle.entity


import org.skycastle.network.Message
import org.skycastle.entity.accesscontrol.users
import entitycontainer.SimpleEntityContainer
import org.scalatest.{Suite, BeforeAndAfter}
import org.skycastle.util.{Property, Parameters}


import org.junit._
import Assert._

import org.skycastle.entity.properties.PropertyConversions._

class TestEntity extends Entity {

  var foo : Int = 0
  var bar : String = ""
  var caller : EntityId= null
  var params : Parameters= null
  var fooList : List[String] = Nil


  /*

  Alternatives:

  // role added in constructor. 
  addRole( 'tester )

  def tester = role member myMembers capability ActionCapability('testAction)  // Role with dynamically defined members and capabilities
  val tester2 = role  capability ActionCapability('testAction2) // statically specified role

  // Pretty flexible
  // Will store editor data on a per instance basis though.  Or it could just accept role function reference.  But not that much data to store in any case, as Symbols are shared.  
  'lunch := "Pizza" editor 'tester  // No direct reference possible
  'lunch := "Pizza" editor tester  // Allows existence checked role
  'lunch := "Pizza" editor tester onChange { newVal => doSomething() }
  val lunch = 'lunch := "Pizza" editor tester  // Can be accessed easier from surrounding code.  Drawback is that id is repeated.

  // assignment
  lunch <= "Chinese"

  // definition
  'lunch := "Chinese"


   */

  var lunch  = 'lunch  :- "Pizza"     // editor 'tester
  var dinner = 'dinner :- "Sphagetti" // editor 'tester

  'breakfast :- "Coffeine 100 mg" // reader 'tester

  @users( "tester")
  @parameters( "newValue" )
  def setFoo( value : Int ) {
    foo = value
  }

  @users( "tester")
  @parameters( "bar, foo" )
  def update( b : String, f: List[String] ) {
    fooList = f
    bar = b
  }

  @users( "tester")
  @parameters( "$callerId, $parameters" )
  def special( c : EntityId, p : Parameters ) {
    caller= c
    params= p
  }


}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class EntityTest extends Suite with BeforeAndAfter {



  var testEntity : TestEntity = null
  var callerEntityid : EntityId = null

  override def beforeEach = {
    testEntity = new TestEntity()
    callerEntityid = EntityId( "entity_testCaller" )
    testEntity.addRole('tester )
    testEntity.addRoleMember( 'tester, callerEntityid )
  }

  def testEntityGetsIdWhenAddedToContainer {
    val entity = new Entity()
    val entityContainer = new SimpleEntityContainer()

    assertNull( entity.id )
    entityContainer.storeEntity( entity, null )
    assertNotNull( entity.id )
    assertEquals( entityContainer,  entity.container  )
  }

  def testRoles {

    // TODO: Easier tests for property editing and calling -> create role collection wrapper.

  }

  def testManualSetProperty {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.lunch := "Italian"
    assert(  "Italian" === testEntity.lunch.value )
  }

  def testSetProperty {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.call( Message( callerEntityid, testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Chinese" ) ) )
    assert(  "Chinese" === testEntity.lunch.value )
  }

  def testSetPropertyWithoutPermission {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.call( Message( EntityId( "AnonymousCoward" ), testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Hot Grits" ) ) )
    assert(  "Pizza" === testEntity.lunch.value )
  }


  def testActionMethodsCallWithPrimitiveArgument {
    assert( 0 === testEntity.foo )
    testEntity.call( callerEntityid, 'setFoo, Parameters( 'newValue -> 1 ) )
    assert( 1 === testEntity.foo )
  }

  def testActionMethodCallWithArgumentList {
    assertEquals( Nil, testEntity.fooList )
    assertEquals( "", testEntity.bar )
    testEntity.call( callerEntityid, 'update, Parameters( 'bar -> "news flash", 'foo -> List("bear", "badger") ) )
    assertEquals( List("bear", "badger"), testEntity.fooList )
    assertEquals( "news flash", testEntity.bar )
  }

  def testActionMethodCallWithSpecialArguemnts {
    assertEquals( null, testEntity.caller )
    assertEquals( null, testEntity.params )
    val params = Parameters( 'bar -> "foo", 'foo -> 1 )
    testEntity.call( callerEntityid, 'special, params )
    assertEquals( callerEntityid, testEntity.caller )
    assertEquals( params, testEntity.params )
  }

}






package org.skycastle.entity


import accesscontrol.{Role, users}
import org.skycastle.network.Message
import entitycontainer.SimpleEntityContainer
import org.scalatest.{Suite, BeforeAndAfter}
import org.skycastle.util.Parameters


import org.junit._
import Assert._

import org.skycastle.entity.properties.PropertyConversions._

class TestEntity extends Entity {

  var foo : Int = 0
  var fooList : List[String] = Nil
  var bar : String = ""
  var caller : EntityId= null
  var params : Parameters= null

  val chef = addRole('chef )

  val lunch  = 'lunch  :- "Pizza"     editor chef
  val dinner = 'dinner :- "Sphagetti" editor chef

  'breakfast :- "Coffeine 100 mg" reader chef

  @users( "chef")
  @parameters( "newValue" )
  def setFoo( value : Int ) {
    foo = value
  }

  @users( "chef")
  @parameters( "bar, foo" )
  def update( b : String, f: List[String] ) {
    fooList = f
    bar = b
  }

  @users( "chef")
  @parameters( "$callerId, $parameters" )
  def special( c : EntityId, p : Parameters ) {
    caller= c
    params= p
  }


}

/**
 * Test Entity.
 * 
 * @author Hans Haggstrom
 */
class EntityTest extends Suite with BeforeAndAfter {
  
  var testEntity : TestEntity = null
  var callerEntityId : EntityId = null

  override def beforeEach = {
    testEntity = new TestEntity()
    callerEntityId = EntityId( "entity_testCaller" )
    testEntity.addRoleMember( 'chef, callerEntityId )
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
    // TODO: Some role testing could be moved to the access control package also

  }

  def testManualSetProperty {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.lunch := "Italian"
    assert(  "Italian" === testEntity.lunch.value )
  }

  def testSetProperty {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.call( Message( callerEntityId, testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Chinese" ) ) )
    assert(  "Chinese" === testEntity.lunch.value )
  }

  def testSetPropertyWithoutPermission {
    assert(  "Pizza" === testEntity.lunch.value )
    testEntity.call( Message( EntityId( "AnonymousCoward" ), testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Hot Grits" ) ) )
    assert(  "Pizza" === testEntity.lunch.value )
  }


  def testActionMethodsCallWithPrimitiveArgument {
    assert( 0 === testEntity.foo )
    testEntity.call( callerEntityId, 'setFoo, Parameters( 'newValue -> 1 ) )
    assert( 1 === testEntity.foo )
  }

  def testActionMethodCallWithArgumentList {
    assertEquals( Nil, testEntity.fooList )
    assertEquals( "", testEntity.bar )
    testEntity.call( callerEntityId, 'update, Parameters( 'bar -> "news flash", 'foo -> List("bear", "badger") ) )
    assertEquals( List("bear", "badger"), testEntity.fooList )
    assertEquals( "news flash", testEntity.bar )
  }

  def testActionMethodCallWithSpecialArguemnts {
    assertEquals( null, testEntity.caller )
    assertEquals( null, testEntity.params )
    val params = Parameters( 'bar -> "foo", 'foo -> 1 )
    testEntity.call( callerEntityId, 'special, params )
    assertEquals( callerEntityId, testEntity.caller )
    assertEquals( params, testEntity.params )
  }

}






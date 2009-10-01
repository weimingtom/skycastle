package org.skycastle.entity


import _root_.junit.framework.TestCase
import org.skycastle.network.Message
import org.skycastle.entity.accesscontrol.users
import entitycontainer.SimpleEntityContainer

import org.junit._
import Assert._
import util.Parameters
import org.scalatest.{Suite, BeforeAndAfter}
import org.skycastle.util.{Property, Parameters}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class EntityTest extends Suite with BeforeAndAfter {

  class TestEntity extends Entity {

    var foo : Int = 0
    var bar : String = ""
    var caller : EntityId= null
    var params : Parameters= null
    var fooList : List[String] = Nil

    @editors( "tester" )
    var lunch : String = "Pizza"

    @editors( "tester" )
    var dinner = Property( "Sphagetti" )

    @readers( "tester" )
    var breakfast : String = "Coffeine 100 mg"

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

  def testSetProperty {
    assertEquals(  "Pizza", testEntity.lunch )
    testEntity.call( Message( callerEntityid, testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Chinese" ) ) )
    assertEquals(  "Chinese", testEntity.lunch )
  }

  def testSetPropertyWithoutPermission {
    assertEquals(  "Pizza", testEntity.lunch )
    testEntity.call( Message( EntityId( "AnonymousCoward" ), testEntity.id, 'setProperty, Parameters( 'property -> 'lunch, 'value -> "Hot Grits" ) ) )
    assertEquals(  "Pizza", testEntity.lunch )
  }


  def testActionMethodsCallWithPrimitiveArgument {
    assertEquals( 0, testEntity.foo )
    testEntity.call( callerEntityid, 'setFoo, Parameters( 'newValue -> 1 ) )
    assertEquals( 1, testEntity.foo )
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






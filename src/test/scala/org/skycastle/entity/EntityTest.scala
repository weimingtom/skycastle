package org.skycastle.entity


import _root_.junit.framework.TestCase
import org.skycastle.entity.accesscontrol.users
import entitycontainer.SimpleEntityContainer

import org.junit._
import Assert._
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@Test
class EntityTest extends TestCase {

  class TestEntity extends Entity {

    var foo : Int = 0
    var bar : String = ""
    var caller : EntityId= null
    var params : Parameters= null
    var fooList : List[String] = Nil

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

  override def setUp = {
    testEntity = new TestEntity()
    callerEntityid = EntityId( "entity_testCaller" )
    testEntity.addRole('tester )
    testEntity.addRoleMember( 'tester, callerEntityid )
  }

  @Test
  def testEntityGetsIdWhenAddedToContainer {
    val entity = new Entity()
    val entityContainer = new SimpleEntityContainer()

    assertNull( entity.id )
    entityContainer.storeEntity( entity )
    assertNotNull( entity.id )
    assertEquals( entityContainer,  entity.container  )
  }




  @Test
  def testActionMethodsCallWithPrimitiveArgument {
    assertEquals( 0, testEntity.foo )
    testEntity.call( callerEntityid, 'setFoo, Parameters( 'newValue -> 1 ) )
    assertEquals( 1, testEntity.foo )
  }

  @Test
  def testActionMethodCallWithArgumentList {
    assertEquals( Nil, testEntity.fooList )
    assertEquals( "", testEntity.bar )
    testEntity.call( callerEntityid, 'update, Parameters( 'bar -> "news flash", 'foo -> List("bear", "badger") ) )
    assertEquals( List("bear", "badger"), testEntity.fooList )
    assertEquals( "news flash", testEntity.bar )
  }

  @Test
  def testActionMethodCallWithSpecialArguemnts {
    assertEquals( null, testEntity.caller )
    assertEquals( null, testEntity.params )
    val params = Parameters( 'bar -> "foo", 'foo -> 1 )
    testEntity.call( callerEntityid, 'special, params )
    assertEquals( callerEntityid, testEntity.caller )
    assertEquals( params, testEntity.params )
  }

}






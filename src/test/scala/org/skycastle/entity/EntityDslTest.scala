package org.skycastle.entity

import org.junit._
import Assert._
import org.scalatest.Suite

/**
 * Spike for testing out Entity DSL ideas.
 */
class EntityDslTest extends Suite {
/*

  case class Property[T]( private var _value : T, host : DslEntity ) {
    var listeners : List[ T => Unit ] = Nil
    def propertyInfo = "This is property with value " + value

    def value : T = _value

    def := ( newValue : T ) {
      _value = newValue
      listeners foreach { x => x( newValue ) }
    }

    def -- ( newValue : T )(implicit hostEntity : DslEntity ) {
      _value = newValue
      hostEntity.addProperty( this )
      listeners foreach { x => x( newValue ) }
    }

    def addListener( listener : T => Unit ) = listeners = listener :: listeners
  }

  implicit def PropertyToValue[T]( property : Property[T] ) : T = property.value

  class DslEntity {

    var properties : Map[PropertyId, Property[_]] = Map()

    def addProperty( name : Symbol, property : Property[_] ) {
      if (properties.contains(name) ) throw new IllegalArgumentException( "Property '"+name+"' already exists." )
      else properties = properties + name -> property
    }

    def apply[T]( symbol : Symbol, default : T ) : T = get( symbol, default)

    def get[T]( symbol : Symbol, default : T ) : T = {
      properties.get(symbol) match {
        case Some( v : Property[T] ) => v.value.asInstanceOf[T]
        case _ => default
      }
    }

    def ?[T]( symbol : Symbol) : T = {
      properties(symbol).value.asInstanceOf[T]
    }

    implicit def symbolToPropertyName( symbol : Symbol ) : PropertyId = PropertyId(symbol)

    implicit def hostEntity : DslEntity = this

    def prop[T]( value : T ) : Property[T] = Property(value)



    object p {
      def > [T]( value : T ) : Property[T] = Property(value, hostEntity)
    }

    object role {
    }
  }

  case class PropertyId( id : Symbol ) {
    def := (value : Any)(implicit host : DslEntity) {
      // Assign parameter
      host.properties = host.properties + this -> value
    }
  }


  class DummyEntity extends DslEntity {

    val otherDsl : DslEntity = new DslEntity()

    // This looks quite pretty, but it looses the type information, and doesn't allow direct . access.
    'name      := "Igor"
    'hitpoints := 10
    'mana      := 100
    'inventory := List( 'torch, 'knife, 'rubberduck )

    // How to add allowed editors / viewers, documentation, ranges, types?, etc?, setting value with message
    // They should only be stored in one place for all instances of a class.  Maybe cached creation..
    // Wastes some cycles each time an object is created to check the cache, but so does an on-the-fly
    // annotation approach too..
    // But caching fails kind of as the information is lost after the first initialization, so not available after deserializaiton..
/*
    val name      = 'name      -- "Igor"
    val hitpoints = 'hitpoints -- 50
    val mana      = 'mana      -- 100
    val inventory = 'inventory -- List( 'torch, 'knife, 'rubberduck )
*/

    def editor = role 

    def name      = p> "noodles"
    def hitpoints = p> 120
    def mana      = p> 120
    def inventory = p> List( 'torch, 'knife, 'rubberduck )
/*
    def eat = action :=
*/

  }


  def testEntityDsl {

    val d = new DummyEntity()

    assert( d( 'hitpoints, 5 ) === 10  )
    assert( d( 'inventory, Nil ).size === 3  )

    assert( d.hitpoints === 10 )
    assert( d.name === "Igor" )
    assert( d.hitpoints.propertyInfo === "This is property with value 10" )
    assert( d.mana === 100 )

    var listenerCalled = false
    d.mana.addListener( x => listenerCalled = true )
    d.mana := 200

    assert( d.mana === 200 )
    assert( listenerCalled === true )

  }


*/


}
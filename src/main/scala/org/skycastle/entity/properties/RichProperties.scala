package org.skycastle.entity.properties

import org.skycastle.entity.accesscontrol.Role
import org.skycastle.util.ClassUtils

/**
 * Provides support for properties that can be listened to, that have access control, and that can be queried.
 * <p>
 * The syntax for defining a property is:
 *
 * <pre>
 * 'propertyName :- propertyValue
 * </pre>
 *
 * Optionally property value can be followed by access control specification, listener addition, etc.
 *
 * <pre>
 * 'propertyName :- propertyValue editor 'editorRole whenChanged listenerFunction
 * </pre>
 *
 * The property can be assigned to a val for easier access, or just defined in a subclass constructor or method.
 *
 * <pre>
 * val propertyHandle = 'propertyName :- propertyValue
 * </pre>
 *
 * The value of a property can be changed with :=, and read directly or with .value:
 *
 * <pre>
 * propertyHandle := newValue
 * println propertyHandle
 * println propertyHandle.value
 * </pre>
 *
 * Symbols can be used to refer to the corresponding property inside the class extending this trait,
 * but for getting the value .value needs to be used in this case:
 *
 * <pre>
 * 'propertyName := newValue
 * println 'propertyName        // Will just print the symbol name instead of the property value
 * println 'propertyName.value  // Will print the property value
 * </pre>
 *
 *
 */
trait RichProperties {

  private var properties : Map[Symbol, RichProperty[_]] = Map()

  case class ValueType[T]( kind : Class[T] )

  case class PropertyMaker(id : Symbol) {
    def :- [T] ( value : T ) : RichProperty[T] = {
      val property = new RichProperty[T]( id, value, ClassUtils.getType( value ) )
      properties = properties + id -> property
      property
    }
  }

  class RichProperty[T](id : Symbol, var _value : T, var kind : Class[_] ) {
    private var editors    : List[Role]         = Nil
    private var readers    : List[Role]         = Nil
    private var listeners  : List[T => Unit]    = Nil
    private var invariants : List[T => Boolean] = Nil

    def editor( editor : Role ) : RichProperty[T]= {
      editors = editor :: editors
      this
    }

    def reader( reader : Role ) : RichProperty[T]= {
      readers = reader :: readers
      this
    }

    def setType( c : Class[_] ) : RichProperty[T]= {
      require( c != null )
      kind = c
      this
    }

    def onChange( listener : T => Unit ) : RichProperty[T] = {
      listeners = listeners ::: List(listener)
      this
    }

    def invariant( check : T => Boolean ) : RichProperty[T]= {
      if (!check( _value ))
        throw new IllegalArgumentException( "Invalid value '"+_value+"' of property when adding an invariant " +
                                            "to property '"+id.name+"' of object '"+hostObject.toString+"'" )
      invariants = invariants ::: List( check )
      this
    }

    def value : T = _value

    def := ( newValue : T) = setValue( newValue )
    
    def setValue( newValue : T) {
      if ( !kind.isAssignableFrom( ClassUtils.getType( newValue ) ) )
        throw new IllegalArgumentException( "Invalid value type when trying to assign value '"+newValue+"' " +
                                            "to property '"+id.name+"' of object '"+hostObject.toString+"'.  The property value should be of type " + kind.getName )
      if (invariants exists { !_(newValue) })
        throw new IllegalArgumentException( "Invalid value when trying to assign value '"+newValue+"' " +
                                            "to property '"+id.name+"' of object '"+hostObject.toString+"'" )

      _value = newValue
    }

  }

  private def hostObject = this

  implicit def symbolToPropertyMaker( id : Symbol ) = PropertyMaker( id )

  implicit def propertyToValue[T]( prop : RichProperty[T] ) : T = prop.value

/*
  implicit def symbolToProperty[T]( id : Symbol ) : RichProperty[T] = {
    properties( id ).asInstanceOf[RichProperty[T]]
  }
*/

}


class RichPropertiesTest extends RichProperties {

  'hitpoints :- 100 invariant {_ > 0} onChange println
  'name      :- null setType classOf[String]

  val mana = 'mana :- 50

  mana := 75

/*
  'hitpoints := 50
*/

  
}



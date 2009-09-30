package org.skycastle.util

object Property{

  val notNull = (v : AnyRef) => v != null
  val anyvalue = (v : Any) => true

  implicit def propertyToValue[T]( property : Property[T] ) : T = property.value


  def apply[T]( value : T ) = new Property[T]( value, anyvalue )
}

/**
 * A variable field that allows listening to changes and enforcing invariants.
 */
case class Property[T]( private var _value : T, invariant : T => Boolean ) {

  private var listeners : List[(T, T) => Unit] = Nil

  checkInvariant(_value)

  def := ( newValue : T) = set( newValue )

  def set(newValue : T) {
    checkInvariant(newValue)

    val oldValue = _value
    _value = newValue
    listeners foreach ( listener => listener( oldValue, newValue ) )
  }

  def addListener( listener : (T, T) => Unit ) {
    listeners = listeners ::: List(listener)
  }

  def removeListener( listener : (T, T) => Unit ) {
    listeners = listeners.remove( _ == listener )
  }

  def value : T = _value

  private def checkInvariant( v : T ) = if (!invariant(v)) throw new IllegalArgumentException( "Can not set property to '"+v+"', the value is not allowed." )

}


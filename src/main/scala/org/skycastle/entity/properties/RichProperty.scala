package org.skycastle.entity.properties

import org.skycastle.entity.accesscontrol.{ReadCapability, EditCapability, Role}
import org.skycastle.util.ClassUtils

/**
 * Import  org.skycastle.entity.properties.PropertyConversions._  to automatically have RichProperties converted
 * to the object they contain when used in expressions and such.
 */
// TODO: In Scala 2.8, place this in the package instead.
object PropertyConversions {
  implicit def propertyToValue[T]( prop : RichProperty[T] ) : T = prop.value
}


/**
 * Property with name and type that contains a value.
 * Allows listening to value changes, and specifying access roles for the property. 
 */
class RichProperty[T]( _id : Symbol, var _value : T, _kind : Class[T] ) {
  private var listeners  : List[T => Unit]    = Nil
  private var invariants : List[T => Boolean] = Nil

  checkKind( _value )

  def editor( editor : Role ) : RichProperty[T]= {
    editor.addCapability( EditCapability( id ) )
    this
  }

  def reader( reader : Role ) : RichProperty[T]= {
    reader.addCapability( ReadCapability( id ) )
    this
  }

  def onChange( listener : T => Unit ) : RichProperty[T] = {
    listeners = listeners ::: List(listener)
    this
  }

  def invariant( invariant : T => Boolean ) : RichProperty[T]= {
    checkInvariant( invariant )
    invariants = invariants ::: List( invariant )
    this
  }

  def id : Symbol = _id

  def kind  : Class[T] = _kind

  def value : T = _value

  def := ( newValue : T) = setValue( newValue )

  def setValue( newValue : T) {
    checkKind( newValue )
    checkInvariants( newValue )

    _value = newValue

    listeners foreach (_(_value))
  }

  private def checkKind( newValue : T ) {
    if ( !kind.isAssignableFrom( ClassUtils.getType( newValue ) ) )
      throw new IllegalArgumentException( "Invalid value type when trying to assign value '"+newValue+"' " +
                                          "to property '"+id.name+"'.  The property value should be of type " + kind.getName )
  }

  private def checkInvariants( newValue : T ) {
    if (invariants exists { !_(newValue) })
      throw new IllegalArgumentException( "Invalid value when trying to assign value '"+newValue+"' " +
                                          "to property '"+id.name+"'." )
  }

  private def checkInvariant( invariant : T => Boolean ) {
    if (!invariant( _value ))
      throw new IllegalStateException( "Invalid value '"+_value+"' of property when adding an invariant " +
                                          "to property '"+id.name+"'." )
  }

}

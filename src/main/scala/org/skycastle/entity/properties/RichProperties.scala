package org.skycastle.entity.properties

import org.skycastle.entity.accesscontrol.Role
import org.skycastle.util.{TypedGetters, ClassUtils}

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
// TODO: Some own exception type for illegal property accesses?
trait RichProperties extends TypedGetters {

  import PropertyConversions._
  
  private var properties : Map[Symbol, RichProperty[_]] = Map()


  def entries : Map[Symbol, Any] = properties.mapElements( _.value ).asInstanceOf[Map[Symbol, Any]]
  

  def ~ (id : Symbol) : RichProperty[Any] = properties( id ).asInstanceOf[RichProperty[Any]]
  def :+ ( id : Symbol ) : PropertyMaker = PropertyMaker( id )

  def getProperty( name : Symbol ) : Option[RichProperty[Any]] = properties.get( name ).asInstanceOf[Option[RichProperty[Any]]]

  def getPropertyValue( name : Symbol ) : Option[Any] = {
    properties.get( name ) match {
      case Some(p) => Some(p.value)
      case None => None
    }
  }

  def apply( name : Symbol ) : RichProperty[Any] = properties( name ).asInstanceOf[RichProperty[Any]]

  def setProperty( name : Symbol, value : Any ) {
    getProperty( name ) match {
      case Some(p) => p := value
      case None => throw new IllegalArgumentException( "No property named '"+name.name+"' found in '"+this.toString+"'." )
    }
  }

  def addProperty[T]( id : Symbol, value : T ) : RichProperty[T] = {
    addProperty( id, value, ClassUtils.getType( value ) )
  }

  def addProperty[T]( id : Symbol, value : T, kind : Class[T] ) : RichProperty[T] = {
    if (properties.contains( id )) throw new IllegalArgumentException( "Can not add property, the property '"+id.name+"' already exists in 'this.toString'." )

    val property = new RichProperty[T]( id, value, kind )
    properties = properties + id -> property
    property
  }

  def removeProperty[T]( id : Symbol ) {
    if (properties.contains( id )) {
      properties = properties - id
    }
  }

  def hasProperty( id : Symbol ) : Boolean = properties.contains( id )

  def getProperties : Map[Symbol, RichProperty[_]] = properties

  implicit def symbolToPropertyMaker( id : Symbol ) = PropertyMaker( id )

  case class PropertyMaker(id : Symbol) {
    def :- [T] ( value : T ) : RichProperty[T] = addProperty( id, value )
    def :/ [T] ( kind : Class[T] ) : PropertyKindMaker[T] = PropertyKindMaker[T]( id, kind )
  }

  case class PropertyKindMaker[T](id : Symbol, kind : Class[T]) {
    def :- ( value : T ) : RichProperty[T] = addProperty( id, value, kind )
  }

  private def hostObject = this

}

object PropertyConversions {
  implicit def propertyToValue[T]( prop : RichProperty[T] ) : T = prop.value
}


class RichProperty[T]( _id : Symbol, var _value : T, _kind : Class[T] ) {
  private var editors    : List[Role]         = Nil
  private var readers    : List[Role]         = Nil
  private var listeners  : List[T => Unit]    = Nil
  private var invariants : List[T => Boolean] = Nil

  checkKind( _value )

  def editor( editor : Role ) : RichProperty[T]= {
    editors = editor :: editors
    this
  }

  def reader( reader : Role ) : RichProperty[T]= {
    readers = reader :: readers
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



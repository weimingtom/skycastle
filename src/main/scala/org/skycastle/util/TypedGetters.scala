package org.skycastle.util

import _root_.org.skycastle.entity.EntityId

/**
 * 
 */

trait TypedGetters {

  def entries : Map[Symbol, Any]

  def has( id : Symbol ) : Boolean = entries.contains( id )

  def get( id : Symbol ) : Option[Any] = entries.get( id )

  // TODO: Convert to fewer different methods with same functionality, and also use a small set of getters so that an implementor can override them if they have a slow entries method. 

  def get[T](id: Symbol, defaultValue: T) : T = entries.getOrElse(id, defaultValue).asInstanceOf[T]


  def getAs[T]( id : Symbol ) : Option[T] = {
    entries.get( id ) match {
      case Some(x) => Some[T]( x.asInstanceOf[T] )
      case None => None
    }
  }

  def getAs[T](id: Symbol, defaultValue: T) : T = {
    val value = entries.getOrElse(id, defaultValue)
    if (value != null)
      value.asInstanceOf[T]
    else defaultValue
  }

  def getOrElse[T]( property : Symbol, default : T ) : T  = entries.getOrElse( property, default ).asInstanceOf[T]

  def getOrElse[T]( id : Symbol, defaultValue : T, kind : Class[T] ) : T  = {
    val value = entries.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if ( kind.isInstance(value)) value.asInstanceOf[T]
    else defaultValue
  }

  //def typeOfProperty( property : Symbol ) : Class[_]    = { null }


  def getBoolean(id: Symbol, defaultValue: Boolean ) : Boolean = getOrElse(id, defaultValue, classOf[Boolean])
  def getInt(id: Symbol, defaultValue: Int ) : Int = getOrElse[Number](id, defaultValue, classOf[Number]).intValue
  def getFloat(id: Symbol, defaultValue: Float ) : Float = getOrElse[Number](id, defaultValue, classOf[Number]).floatValue
  def getLong(id: Symbol, defaultValue: Long ) : Long = getOrElse[Number](id, defaultValue, classOf[Number]).longValue
  def getDouble(id: Symbol, defaultValue: Double ) : Double = getOrElse[Number](id, defaultValue, classOf[Number]).doubleValue
  def getString(id: Symbol, defaultValue: String ) : String = getOrElse[String](id, defaultValue, classOf[String])
  def getSymbol(id: Symbol, defaultValue: Symbol ) : Symbol = getOrElse[Symbol](id, defaultValue, classOf[Symbol])
  def getEntityId(id: Symbol, defaultValue: EntityId ) : EntityId = getOrElse[EntityId](id, defaultValue, classOf[EntityId])

  def getAsString(id: Symbol, defaultValue: String) : String = {
    val s = entries.getOrElse(id, defaultValue)
    if (s == null) defaultValue else s.toString
  }



/*
  def getInt(id: Symbol, defaultValue: Int) : Int = {
    val value = entries.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Number]) value.asInstanceOf[Number].intValue
    else defaultValue
  }

  def getFloat(id: Symbol, defaultValue: Float) : Float= {
    val value = entries.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Number]) value.asInstanceOf[Number].floatValue
    else defaultValue
  }

  def getBoolean(id: Symbol, defaultValue: Boolean ) : Boolean = {
    val value = entries.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Boolean]) value.asInstanceOf[Boolean]
    else defaultValue
  }

  def getString(id: Symbol, defaultValue: String) : String = {
    val s = entries.getOrElse(id, defaultValue)
    if (s == null) defaultValue else s.toString
  }
*/

  def toKeyValueString() : String = toKeyValueString( " "+Parameters.KEY_VALUE_SEPARATOR+" ",
                                                      ""+Parameters.ENTRY_SEPARATOR )

  def toKeyValueString( keyValueSeparator : String, entrySeparator : String ) : String = {
    val s = new StringBuilder
    entries foreach { case( key, value ) =>
      s.append( key.name )
      s.append( keyValueSeparator )
      s.append( value.toString )
      s.append( entrySeparator )
    }

    s.toString
  }

  override def toString = "( " + toKeyValueString( "= ", ", "  ) + " )"


}
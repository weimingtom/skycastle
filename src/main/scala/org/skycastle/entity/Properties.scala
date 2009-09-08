package org.skycastle.entity

import _root_.org.skycastle.entity.entitycontainer.EntityContainer

import util.Parameters

/**
 * A mixin that provides Property support.
 * 
 * @author Hans Haggstrom
 */
trait Properties {

  var container : EntityContainer
  var id : EntityId

  private var properties : Map[Symbol, Any] = Map()

  def setProperties( newProperties : Parameters ) {
    properties = newProperties.properties
  }

  def updateProperties( changed : Parameters ) {
    properties = properties ++ changed.properties
  }

  def toParameters : Parameters = {
    new Parameters( properties )
  }

  def getProperties : Map[Symbol,Any] = properties

  def set( property : Symbol, value : Any ) {
    val entry = (property, value)
    properties = properties + entry
  }

  def get( property : Symbol ) : Option[Any] = properties.get( property )
  
  def getAs[T]( property : Symbol ) : Option[T] = {
    properties.get( property ) match {
      case Some(x) => Some[T]( x.asInstanceOf[T] )
      case None => None
    }
  }

  def getOrElse[T]( property : Symbol, default : T ) : T  = properties.getOrElse( property, default ).asInstanceOf[T]

  def getOrElse[T]( id : Symbol, defaultValue : T, kind : Class[T] ) : T  = {
    val value = properties.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if ( kind.isInstance(value)) value.asInstanceOf[T]
    else defaultValue
  }

  def hasProperty( property : Symbol ) : Boolean = properties.contains( property )
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
    val s = properties.getOrElse(id, defaultValue)
    if (s == null) defaultValue else s.toString
  }

  def getReferencedEntity( referenceproperty : Symbol ) : Option[ Entity ]  = {
    val entityId = getEntityId( referenceproperty, null )
    if (entityId == null || container == null) {
      None
    }
    else {
      container.getEntity( entityId )
    }
  }

  def getReferencedEntityForUpdate( referenceproperty : Symbol ) : Option[ Entity ]  = {
    val entityId = getEntityId( referenceproperty, null )
    if (entityId == null || container == null) {
      None
    }
    else {
      container.getEntityForUpdate( entityId )
    }
  }

  def requestParameter( listener : EntityActionId, property : Symbol ) {
    requestParameters( listener, ParametersExpression( Map() ) )
  }
  def requestParameters( listener : EntityActionId, parameterSources : ParametersExpression )  = {
    container.call( id, listener, parameterSources.getParameters( this ) )
  }


/* TODO: Implement

  def addListener( property : Symbol, listener : EntityActionId, parameterSources : ParametersExpression )  = { null }

  def addTrigger( property : Symbol,  trigger : Trigger, listener : EntityActionId, parameterSources : ParametersExpression)  = { null }
*/
}


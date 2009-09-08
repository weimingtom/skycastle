package org.skycastle.entity


import util.Parameters

/**
 * A mixin that provides Property support.
 * 
 * @author Hans Haggstrom
 */
trait Properties {

  def setProperties( changed : Parameters )   = { null }
  def getPropertiesAsParameters : Parameters    = { null }
  def properties : Iterator[ Pair[Symbol,Any] ]   = { null }

  def set( property : Symbol, value : Any )   = { null }
  def get( property : Symbol ) : Option[Any]    = { null }
  def getAs[T]( property : Symbol ) : Option[T]    = { null }
  def getOrElse[T]( property : Symbol, default : T ) : T    = { default }

  def has( property : Symbol ) : Boolean    = { false }
  def typeOf( property : Symbol ) : Class[_]    = { null }

  def getBoolean( property : Symbol, default : Int ) : Boolean    = { false }
  def getInt( property : Symbol, default : Int ) : Int    = { 0 }
  def getLong( property : Symbol, default : Long ) : Long    = { 0 }
  def getFloat( property : Symbol, default : Float ) : Float    = { 0}
  def getDouble( property : Symbol, default : Double ) : Double  = { 0}
  def getString( property : Symbol, default : String ) : String  = { null }
  def getSymbol( property : Symbol, default : Symbol ) : Symbol    = { null }
  def getEntityId( property : Symbol, default : EntityId ) : EntityId  = { null }

  def getReferencedEntity( referenceproperty : Symbol ) : Option[ Entity ]  = { null }
  def getReferencedEntityForUpdate( referenceproperty : Symbol ) : Option[ Entity ]  = { null }

  def requestParameter( listener : EntityActionId, property : Symbol ) {
    requestParameters( listener, ParametersExpression( Map() ) )
  }
  def requestParameters( listener : EntityActionId, parameterSources : ParametersExpression )  = { null }

  def addListener( property : Symbol, listener : EntityActionId, parameterSources : ParametersExpression )  = { null }
  def addTrigger( property : Symbol,  trigger : Trigger, listener : EntityActionId, parameterSources : ParametersExpression)  = { null }
}


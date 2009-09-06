package org.skycastle.entity


import org.skycastle.entity.Expression
import util.Parameters

/**
 * A mixin that provides Property support.
 * 
 * @author Hans Haggstrom
 */
trait Properties {

  def setProperties( changed : Parameters )
  def getPropertiesAsParameters : Parameters
  def properties : Iterator[ Pair[Symbol,Any] ]

  def set( property : Symbol, value : Any )
  def get( property : Symbol ) : Option[Any]
  def getAs[T]( property : Symbol ) : Option[T]
  def getOrElse[T]( property : Symbol, default : T ) : T

  def has( property : Symbol ) : Boolean
  def typeOf( property : Symbol ) : Class

  def getBoolean( property : Symbol, default : Int ) : Boolean
  def getInt( property : Symbol, default : Int ) : Int
  def getLong( property : Symbol, default : Long ) : Long
  def getFloat( property : Symbol, default : Float ) : Float
  def getDouble( property : Symbol, default : Double ) : Double
  def getString( property : Symbol, default : String ) : String
  def getSymbol( property : Symbol, default : Symbol ) : Symbol
  def getEntityId( property : Symbol, default : EntityId ) : EntityId

  def getReferencedEntity( referenceproperty : Symbol ) : Option[ Entity ]
  def getReferencedEntityForUpdate( referenceproperty : Symbol ) : Option[ Entity ]


  def addListener( property : Symbol, targetEntity : EntityId, calledAction : Symbol, Map[Symbol, Expression] )
  def addTrigger( property : Symbol,  trigger : Expression, targetEntity : EntityId, calledAction : Symbol, Map[Symbol, Expression])
}


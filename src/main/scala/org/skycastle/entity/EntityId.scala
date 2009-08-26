package org.skycastle.entity


import java.io.Serializable

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@SerialVersionUID( 1 )
final case class EntityId( id : String ) extends Serializable {
  def managedObjectName = id
  override def toString = managedObjectName

}


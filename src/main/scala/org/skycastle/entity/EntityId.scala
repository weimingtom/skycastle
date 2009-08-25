package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class EntityId( id : Long ) {

  override def toString = "Entity-" + id

}


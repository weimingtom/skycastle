package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class EntityId( id : Long ) {

  override def toString = "Entity-" + id

}


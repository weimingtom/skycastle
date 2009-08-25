package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ArchetypeId( id : String, revision : Long ) {
  def managedObjectName = id + "-r"+revision
  override def toString = managedObjectName
}


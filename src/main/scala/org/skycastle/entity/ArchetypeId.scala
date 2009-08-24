package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */
final case class ArchetypeId( id : String, revision : Long ) {
  override def toString = "Archetype " + id + "(r"+revision+")"
}


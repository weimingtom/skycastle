package org.skycastle.server


import com.sun.sgs.app.ManagedObject
import entity.Archetype

/**
 * ManagedObject wrapper for an Archetype
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedArchetype( archetype : Archetype ) extends ManagedObject {

}


package org.skycastle.server


import com.sun.sgs.app.ManagedObject
import entity.Entity

/**
 * ManagedObject wrapper for an Entity
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedEntity( entity : Entity ) extends ManagedObject {

}


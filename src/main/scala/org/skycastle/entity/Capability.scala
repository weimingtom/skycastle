package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */

abstract class Capability

/**
 * The capability to call any action on the entity and change any capability of the Entity.
 */
case class Administration extends Capability

/**
 * The capability to call a specific action on an Entity.
 */
case class ActionCapability( actionId : String ) extends Capability

/**
 * The capability to grant and remove action capabilities to CallerId:s for an Entity.
 */
case class ActionManagementCapability( actionId : String ) extends Capability




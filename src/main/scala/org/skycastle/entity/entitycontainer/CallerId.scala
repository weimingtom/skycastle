package org.skycastle.entity.entitycontainer

/**
 * Represents something that can have access rights for calling actions.
 * Can be an external user, a subsystem, an in-game character, or some game object such as an organization(?).
 * 
 * @author Hans Haggstrom
 */
final case class CallerId( callerIdentity : String )

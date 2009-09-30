package org.skycastle.entity.accesscontrol

/**
 * Represents different capabilities that a role cna have for an Entity.
 * 
 * @author Hans Haggstrom
 */
abstract class Capability {
  def allowsCall( action : Symbol ) : Boolean = false
  def allowsRead( property : Symbol ) : Boolean = false
  def allowsWrite( property : Symbol ) : Boolean = false
}

/**
 * The capability to call a specific action on an Entity.
 */
case class ActionCapability( actionId : Symbol ) extends Capability {
  override def allowsCall(action: Symbol) = action == actionId
}

/**
 * The capability to call any action on the entity.
 */
case object AllActionCapability extends Capability {
  override def allowsCall(action: Symbol) = true
}

/**
 * The capability to read a specific property of an Entity.
 */
case class ReadCapability( propertyId : Symbol ) extends Capability {
  override def allowsRead(property: Symbol) = property == propertyId
}

/**
 * The capability to write a specific property of an Entity.
 */
case class WriteCapability( propertyId : Symbol ) extends Capability {
  override def allowsWrite(property: Symbol) = property == propertyId
}

/**
 * The capability to edit (both read and write) a specific property of an Entity.
 */
case class EditCapability( propertyId : Symbol ) extends Capability {
  override def allowsRead(property: Symbol) = property == propertyId
  override def allowsWrite(property: Symbol) = property == propertyId
}



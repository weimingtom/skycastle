package org.skycastle.entity.accesscontrol

/**
 * Represents different capabilities that a role can have for an Entity.
 * 
 * @author Hans Haggstrom
 */
abstract class Right {
  def allowsCall( action : Symbol ) : Boolean = false
  def allowsRead( property : Symbol ) : Boolean = false
  def allowsWrite( property : Symbol ) : Boolean = false
}

/**
 * The capability to call a specific action on an Entity.
 */
case class CallRight( actionId : Symbol ) extends Right {
  override def allowsCall(action: Symbol) = action == actionId
}

/**
 * The capability to call any action on the entity.
 */
case object AllCallRight extends Right {
  override def allowsCall(action: Symbol) = true
}

/**
 * The capability to read a specific property of an Entity.
 */
case class ReadRight( propertyId : Symbol ) extends Right {
  override def allowsRead(property: Symbol) = property == propertyId
}

/**
 * The capability to write a specific property of an Entity.
 */
case class WriteRight( propertyId : Symbol ) extends Right {
  override def allowsWrite(property: Symbol) = property == propertyId
}

/**
 * The capability to edit (both read and write) a specific property of an Entity.
 */
case class EditRight( propertyId : Symbol ) extends Right {
  override def allowsRead(property: Symbol) = property == propertyId
  override def allowsWrite(property: Symbol) = property == propertyId
}



package org.skycastle.entity



import _root_.scala.{Option}
import accesscontrol.RoleMember
import util.ParameterChecker._

object EntityId {

  def apply( id : String  ) = new EntityId( List( id ) )

  def decode( bridgeId : EntityId, path : List[String] ) : EntityId = {
    val finalPath = if ( path.head == REMOTE_OBJ_ENCODING ) bridgeId.path ::: path.tail
                    else path 


    new EntityId( finalPath )
  }

  val REMOTE_OBJ_ENCODING :String = "0"

}


/**
 * Identifies an Entity in this EntityContainer, or in some other EntityContainer accessible through a specified path.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class EntityId( path : List[String] ) extends RoleMember {

  requireNotEmpty( path, 'path )
  path foreach ( requireIsIdentifier( _, 'path_element ) )

  def managedObjectName : Option[String] = {
    // It's only a managed object if it is hosted in the current container.
    if (path.size == 1) Some( path.head )
    else None
  }

  def containsEntity(entity: EntityId) = entity.path == path

  def headEntityId : EntityId = {
    if (path.size <= 1) this
    else new EntityId( List( path.head ) )
  }

  def tailEntityId : Option[ EntityId ] = {
    if (path.size <= 1) None
    else Some( new EntityId( path.tail ) )
  }

  override def toString = path.mkString( "." )

  def encode( bridgeId : EntityId ) : List[String] = {
    if ( path.size > 1 &&  path.head == bridgeId ) path.tail
    else List( EntityId.REMOTE_OBJ_ENCODING ) ::: path
  }

}


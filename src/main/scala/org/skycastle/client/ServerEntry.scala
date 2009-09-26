package org.skycastle.client
import org.skycastle.util.Properties


import entity.accesscontrol.{ActionCapability, Capability}
import entity.{Entity, EntityId}
import ui.ScreenEntity

/**
 * Holds data about a server on a client.
 *
 * Can be used to create a connection to the server, with a specified user name, or to register a new user on the
 * server if possible.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ServerEntry extends Entity {

  /**
   * Start connecting to the server.
   * The connection progress and server provided UI are shown on the referenced ScreenEntity.
   */
  // TODO: Provide the password here?
  def connect( username : String, screenId : EntityId ) {

    val serverUrl = getString( 'serverUrl, null )
    val serverPort = getString( 'serverPort, null )

    if (serverUrl == null) throw new IllegalArgumentException("Server URL can not be null")
    if (screenId == null) throw new IllegalArgumentException("Screen ID can not be null")

    container.getEntity( screenId ) match {
      case None =>throw new IllegalArgumentException("No screen with id "+screenId+" found")
      case Some(screen : ScreenEntity) => {

        val connection = new UserConnection()
        container.storeEntity( connection, null )

        // Make sure the UserConnection can update its UI:
        screen.addRoleMember( 'editor, connection.id )

        connection.connect()

      }
    }

  }

}


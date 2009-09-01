package org.skycastle.client


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
class ServerEntry extends Entity {

  /**
   * Start connecting to the server.
   * The connection progress and server provided UI are shown on the referenced ScreenEntity.
   */
  // TODO: Provide the password here?
  def connect( username : String, screenId : EntityId ) {

    val serverUrl = properties.getString( 'serverUrl, null )
    val serverPort = properties.getString( 'serverPort, null )

    if (serverUrl == null) throw new IllegalArgumentException("Server URL can not be null")
    if (screenId == null) throw new IllegalArgumentException("Screen ID can not be null")

    container.getEntity( screenId ) match {
      case Null =>throw new IllegalArgumentException("No screen with id "+screenId+" found")
      case Some(screen : ScreenEntity) => {

        val connection = new UserConnection()
        container.storeEntity( connection )

        // Make sure the UserConnection can update its UI:
        screen.createUiEditorRole()
        screen.addRoleMember( "uiEditor", connection.id )

        connection.connect()

      }
    }

  }

}

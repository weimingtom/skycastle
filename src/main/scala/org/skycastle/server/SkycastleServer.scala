package org.skycastle.server

import _root_.org.skycastle.entity.EntityId
import com.sun.sgs.app._
import content.account.server.{ServerSideAccountEntity, AccountManagedObject}
import entity.entitycontainer.darkstar.DarkstarEntityContainer
import entity.tilemap.{TilemapEntity}
import java.util.Properties


/**
 * Server side entrypoint for server initialization & connecting users
 *
 * @author Hans Haggstrom
 */
@SerialVersionUID(1)
@serializable
class SkycastleServer extends AppListener {


  /**
   * This is called to initialize the server
   */
  def initialize(properties: Properties) {

    ServerLogger.logger.info("Skycastle Server Started")

    // Try to load an initial game from a specified file in a default storage format
    // TODO

    // If no initial game specified, instantiate the standard game (which allows the admin to set up & edit games).
    // TODO

    // Store a reference to the top level game that users are added to on login.
    // TODO

  }

  /**
   * This is called when a user connects
   */
  def loggedIn(session: ClientSession) : ClientSessionListener  = {

    val USER_ID_PREFIX = "user-"
    val userId = USER_ID_PREFIX + session.getName()

    val dataManager = AppContext.getDataManager()

    // Get the user with the name used in the session login, or if not found, creates a new one.
    val account: AccountManagedObject = try {
      dataManager.getBinding(userId).asInstanceOf[AccountManagedObject]
    } catch {
      case e: NameNotBoundException => {
        ServerLogger.logInfo( "New player account created: " + userId)
        val a = new AccountManagedObject( new ServerSideAccountEntity() )

        DarkstarEntityContainer.storeManagedEntity( a )

        // TODO: Join user to the top-level Game on the Server.

        dataManager.setBinding(userId, a)

        a
      }
    }

    account.setSession( session )

    account
  }

}


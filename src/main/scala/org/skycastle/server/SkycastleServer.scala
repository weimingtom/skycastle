package org.skycastle.server

import _root_.org.skycastle.entity.EntityId
import com.sun.sgs.app._
import content.account.server.{ServerSideAccountEntity, AccountManagedObject}
import content.activities.activitybrowser.ActivityBrowser
import content.activity.ActivityEntity
import entity.entitycontainer.darkstar.DarkstarEntityContainer
import entity.tilemap.{TilemapEntity}
import java.util.Properties
import skycastle.util.Parameters


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


    // TODO: Create initial activity types..
    val textEditorFactoryId : EntityId = null

    val topLevelActivityBrwoser : ActivityBrowser = new ActivityBrowser()
    topLevelActivityBrwoser.addActivityType( 'textEditor, textEditorFactoryId, Parameters() )
    DarkstarEntityContainer.bindName( "topLevelActivity", topLevelActivityBrwoser )

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

        dataManager.setBinding(userId, a)

        a.init()

        // TODO: Send user some interface or similar for joining server activities etc.
        a
      }
    }

    account.setSession( session )

    account
  }

}


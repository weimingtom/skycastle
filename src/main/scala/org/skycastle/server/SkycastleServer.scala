package org.skycastle.server

import com.sun.sgs.app._
import content.account.client.ClientSideAccountEntity
import content.account.server.{ServerSideAccountEntity, AccountManagedObject}
import content.activities.activitybrowser.ActivityBrowser
import content.activities.chat.Conversation
import content.activities.textedit.TexteditActivity
import content.activity.ActivityEntity
import content.factory.SimpleEntityFactory
import entity.entitycontainer.darkstar.DarkstarEntityContainer
import entity.tilemap.{TilemapEntity}
import entity.{Entity, EntityId}
import java.util.Properties
import network.Message
import skycastle.util.Parameters


/**
 * Server side entrypoint for server initialization & connecting users
 *
 * @author Hans Haggstrom
 */
@SerialVersionUID(1)
@serializable
class SkycastleServer extends AppListener {

  var topLevelActivityId : EntityId = null

  var serverParameters : Parameters = null

  /**
   * This is called to initialize the server
   */
  def initialize(properties: Properties) {

    ServerLogger.logger.info("Skycastle Server Started")

    // Try to load an initial activity from a specified file in a default storage format
    // TODO

    // If no initial activity is specified, instantiate the standard activity browser with some example applications:

    // Set up activity browser
    val browser : ActivityBrowser = new ActivityBrowser()
    topLevelActivityId = DarkstarEntityContainer.storeEntity( browser, null )
    DarkstarEntityContainer.bindName( "topLevelActivity", browser )

    // Create initial activity types
    createActivityType( browser, "Text Editor", "Collaborative multi-user text editor!", classOf[TexteditActivity], Parameters() )
    createActivityType( browser, "Chat", "Multi-user conversation!", classOf[Conversation], Parameters() )

    // Setup server parameters
    // TODO: Get the properties from the application properties or similar?
    val application = "Skycastle"
    val version     = "0.1.0"
    val build       = "r?"
    val serverName  = "Skycastle Test Server"
    val serverDescription  = "For testing the Skycastle Server.  May reboot at any time."

    serverParameters = Parameters(
      'application -> application,
      'version -> version,
      'build -> build,
      'serverName -> serverName,
      'serverDescription -> serverDescription,
      'mainActivity -> topLevelActivityId )
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
        // Create account for new player
        ServerLogger.logInfo( "New player account created: " + userId)
        val accountManagedObject = new AccountManagedObject( new ServerSideAccountEntity(), serverParameters )

        DarkstarEntityContainer.storeManagedEntity( accountManagedObject, null )

        dataManager.setBinding(userId, accountManagedObject)

        accountManagedObject.init()

        accountManagedObject
      }
    }

    account.setSession( session )

    account
  }


  private def createActivityType( browser : ActivityBrowser, name : String, description : String, kind : Class[_ <: ActivityEntity], creationParameters : Parameters ) : EntityId = {
    val info = Parameters( 'name -> name, 'description -> description )
    val activityEntity = new SimpleEntityFactory( info, kind.getName, kind, creationParameters, null )
    val activityId = DarkstarEntityContainer.storeEntity( activityEntity, null )
    browser.addActivityType( Symbol(name), activityId )
    activityId
  }

}


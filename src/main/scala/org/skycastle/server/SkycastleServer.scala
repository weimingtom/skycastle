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
import network.negotiator.ProtocolNegotiator
import network.protocol.binary.{BinarySerializer, BinaryProtocol}
import network.protocol.Protocol
import network.{NetworkConnection, Message}
import skycastle.util.{ClassUtils, TimerUtil, Parameters}
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

  private var adminUser : EntityId = null

  /**
   * This is called to initialize the server
   */
  def initialize(properties: Properties) {

    ServerLogger.logger.info("Skycastle Server Started")

    // Preload common classes to speed up subsequent tasks
    ServerLogger.logger.info("Preloading some classes")
    ClassUtils.preloadClasses(
      classOf[Entity],
      classOf[NetworkConnection],
      classOf[AccountManagedObject],
      classOf[ServerSideAccountEntity],
      classOf[ProtocolNegotiator],
      classOf[Message],
      classOf[Protocol],
      classOf[BinarySerializer],
      classOf[BinaryProtocol],
      classOf[ActivityEntity]
      )

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

    // Create an account for the server administrator
    ServerLogger.logger.info("Creating admin user account")
    val adminAccount = createAccount( "admin" )
    adminUser = adminAccount.entity.id

  }

  /**
   * This is called when a user connects
   */
  def loggedIn(session: ClientSession) : ClientSessionListener  = {
    TimerUtil.timeAndLog( "loggedIn user " + session.getName ) {

      val USER_ID_PREFIX = "user-"
      val userId = USER_ID_PREFIX + session.getName()

      val dataManager = AppContext.getDataManager()

      // Get the user with the name used in the session login, or if not found, creates a new one.
      val account: AccountManagedObject = try {
        dataManager.getBinding(userId).asInstanceOf[AccountManagedObject]
      } catch {
        case e: NameNotBoundException => createAccount( userId )
      }

      account.setSession( session )

      account
    }
  }


  def createAccount( userId : String ) : AccountManagedObject = {
    TimerUtil.timeAndLog( "createAccount" ) {
      val accountManagedObject = new AccountManagedObject( new ServerSideAccountEntity(), serverParameters )

      val id = DarkstarEntityContainer.storeManagedEntity( accountManagedObject, null )

      AppContext.getDataManager().setBinding(userId, accountManagedObject)

      accountManagedObject.init()

      ServerLogger.logInfo( "New user account created for user '" + userId + "' with account object '"+id+"'.")

      accountManagedObject
    }
  }


  private def createActivityType( browser : ActivityBrowser, name : String, description : String, kind : Class[_ <: ActivityEntity], creationParameters : Parameters ) : EntityId = {
    val info = Parameters( 'name -> name, 'description -> description )
    val activityEntity = new SimpleEntityFactory( info, kind.getName, kind, creationParameters, null )
    val activityId = DarkstarEntityContainer.storeEntity( activityEntity, null )
    browser.addActivityType( Symbol(name), activityId )
    activityId
  }

}


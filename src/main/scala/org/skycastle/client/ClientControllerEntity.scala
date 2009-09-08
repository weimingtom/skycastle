package org.skycastle.client


import content.account.client.ClientSideAccountEntity
import entity.accesscontrol.ActionCapability
import entity.{EntityId, Entity}
import ui.{ScreenEntity, Ui}
import util.Parameters
/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ClientControllerEntity extends Entity {

  addRole( "connect" )
  addRoleCapability( "connect", ActionCapability( 'connectToServer ) )


  def createUi() {

    val clientUiId = getOrElse( 'uiId, null, classOf[EntityId] )
    val screen : ScreenEntity = container.getEntity( clientUiId ) match {
      case Some( screen : ScreenEntity ) => screen
      case _ => buildClientScreen()
    }

    screen.showFrame
  }

  private def buildClientScreen() : ScreenEntity = {

    // TODO: Create a startup screen that allows the user to select a server from a list fetched from some
    // metaserver system (maybe each server could also mirror other servers location info?),
    // or enter server information manually (that gets saved in on the client using some data
    // storage service or entity serialization storage?),
    // and then connect to the server, specifying username and password, or logging in with an account creation login
    // if the user doesn't have any login yet.
    val screen = new ScreenEntity()
    screen.addComponent( "panel", 'root, null, Parameters( 'text -> "test client" ))
    screen.addComponent( "label", 'label01, 'root, Parameters('text -> "specify server to logon to" ))
    screen.addComponent( "field", 'serverField, 'root, Parameters( 'text -> "testserver", 'tooltip -> "The server to connect to" ) )
    screen.addComponent( "button", 'login, 'root, Parameters( 'text -> "Login", 'calledEntity -> id, 'calledAction -> 'connectToServer, 'actionParameters -> Map( 'url -> 'serverField ) ) )


    container.storeEntity( screen )

    set( 'uiId, screen.id )
    addRoleMember( "connect", screen.id )

    screen
  }



  /**
   * Creates a new client side entity representing the specified server, and initiates a connection.
   * The provided ui is the ui that the server is allowed to manipulate & fill with its own ui
   * (this allows the client to e.g. run several servers in different tabs or screens, or to have an own frame and
   * hotkey catcher around the server provided ui).
   */
  // TODO: Refactor connection to a separate client side server representation object,
  // and have the connection take username etc as parameters
  override protected def callBuiltinAction(actionName: Symbol, parameters: Parameters) = {

    actionName match {
      case 'connectToServer => connectToServer( parameters.getAs[String]('url, null ), parameters.getAs[String]('port, null ), parameters.getAs[String]('userName, null ), null  ) ; true

      case _ => false
    }
    
  }

  def connectToServer( url : String, port : String, userName : String, uiContainerForServer : Ui ) {

    val account = new ClientSideAccountEntity

    container.storeEntity( account )

    account.server = url
    account.port = port
    account.accountName = userName
    account.setPassword( "password".toCharArray )


    println( "Trying to connect to server "+url+":"+port+" as user "+ userName  )
    account.connect
  }
}


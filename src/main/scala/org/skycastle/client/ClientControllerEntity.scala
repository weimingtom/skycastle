package org.skycastle.client


import org.skycastle.content.account.client.ClientSideAccountEntity
import org.skycastle.entity.accesscontrol.{users, ActionCapability}
import org.skycastle.entity.{parameters, EntityId, Entity}
import org.skycastle.ui.{ScreenEntity, Ui}
import org.skycastle.util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ClientControllerEntity extends Entity {

  addRole( 'connect )

  'uiId :- Symbol(null)

  def createUi() {

    val clientUiId = getProperty( 'uiId, null, classOf[EntityId] )
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
    screen.addComponent( "field", 'serverField, 'root, Parameters( 'text -> "localhost", 'tooltip -> "The server to connect to" ) )

    screen.addComponent( "label", 'label02, 'root, Parameters('text -> "port" ))
    screen.addComponent( "field", 'portField, 'root, Parameters( 'text -> "1139", 'toolTip -> "The port" ) )

    screen.addComponent( "label", 'label03, 'root, Parameters('text -> "Username" ))
    screen.addComponent( "field", 'userField, 'root, Parameters( 'text -> "TestUser", 'tooltip -> "The account name on the server to connect to" ) )

    screen.addComponent( "button", 'login, 'root, Parameters( 'text -> "Login", 'calledEntity -> id, 'calledAction -> 'connectToServer, 'actionParameters -> Map( 'url -> 'serverField, 'port -> 'portField, 'userName -> 'userField ) ) )


    container.storeEntity( screen, null )

    setProperty( 'uiId, screen.id )
    addRoleMember( 'connect, screen.id )

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
  @users("connect")
  @parameters( "url, port, userName" )
  def connectToServer( url : String, port : String, userName : String ) {

    val account = new ClientSideAccountEntity
    
    container.storeEntity( account, createClientParameters )

    account.server = url
    account.port = port
    account.accountName = userName
    account.setPassword( "password".toCharArray )


    println( "Trying to connect to server "+url+":"+port+" as user "+ userName  )
    account.connect
  }

  private def createClientParameters : Parameters = {
    // TODO: Get the properties from the application properties or similar?
    val application = "SkycastleClient"
    val version     = "0.1.0"
    val build       = "r?"

    Parameters(
      'application -> application,
      'version -> version,
      'build -> build,
      'mainEntity -> id )
  }
}


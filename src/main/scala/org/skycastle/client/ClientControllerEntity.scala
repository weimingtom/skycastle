package org.skycastle.client


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

  



  def createUi() {

    val clientUiId = properties.getAs[EntityId]( 'uiId, null )
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
    screen.addComponent( "label", 'root, null, Parameters(Map( 'text -> "Welcome to the Skycastle Client" )) )

    container.storeEntity( screen )

    properties.set( 'uiId, screen.id )

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
  def connectToServer( url : String, port : String, ownName : String, uiContainerForServer : Ui ) {

  }
}


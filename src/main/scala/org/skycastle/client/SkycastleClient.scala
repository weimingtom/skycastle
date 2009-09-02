package org.skycastle.client


import com.jme.app.AbstractGame
import entity.entitycontainer.{SimpleEntityContainer, EntityContainer}
import entity.{EntityId, Entity}
import javax.swing.JLabel
import rendering.{MainGameLoop, VisibleEntity}
import util.{SimpleFrame, ResourceLoader}
/**
 * Client side entrypoint 
 * 
 * @author Hans Haggstrom
 */

object SkycastleClient {

  private var entityContainer : EntityContainer = null

  private val clientControllerName = "clientController"

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )

    entityContainer = loadOrCreateEntityContainer

    val clientController = getClientController( entityContainer  )

    clientController.createUi()

    // Testing 3D UI:
    val logoUrl = ResourceLoader.getResourceURL( "images/skycastle-logo.png" )
    val renderer = new MainGameLoop("Skycastle 3D Test", null )
    renderer.setConfigShowMode(  AbstractGame.ConfigShowMode.AlwaysShow, logoUrl   )

    renderer.start();

  }

  private def loadOrCreateEntityContainer : EntityContainer = {
    // Load user configured client container from specified file if available
    // TODO

    // Otherwise create empty container
    new SimpleEntityContainer()
  }

  private def getClientController(container : EntityContainer ) : ClientControllerEntity = {
    // Get the client controller stored in client configuration, or create a new one if not found or if it is the wrong type
    container.getNamedEntityForUpdate( clientControllerName ) match {
      case Some( controller ) => {
        if (controller.isInstanceOf[ClientControllerEntity] ) controller.asInstanceOf[ClientControllerEntity]
        else {
          container.removeBinding( clientControllerName )
          createClientController( container )
        }
      }
      case None => createClientController( container )
    }
  }


  private def createClientController( container : EntityContainer ) : ClientControllerEntity = {

    val controller = new ClientControllerEntity()

    container.bindName( clientControllerName, controller )

    controller
  }

}


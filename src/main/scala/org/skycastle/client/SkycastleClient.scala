package org.skycastle.client


import com.jme.app.AbstractGame
import entity.entitycontainer.{SimpleEntityContainer, EntityContainer}
import entity.{EntityId, Entity, EntityLogger}
import javax.swing.JLabel
import rendering.{MainGameLoop, VisibleEntity}
import skycastle.rendering.Screen3DEntity
import util.{SimpleFrame, ResourceLoader}
/**
 * Client side entrypoint 
 * 
 * @author Hans Haggstrom
 */

object SkycastleClient {

  private var entityContainer : SimpleEntityContainer = null

  private val clientControllerName = "clientController"

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )

    entityContainer = loadOrCreateEntityContainer

    val clientController = getClientController( entityContainer  )

    clientController.createUi()

    entityContainer.start

    // Testing 3D UI:
//    val screen3D = new Screen3DEntity()
//    screen3D.showScreen
/*
    val logoUrl = ResourceLoader.getResourceURL( "images/skycastle-logo.png" )
*/
    null
  }

  private def loadOrCreateEntityContainer : SimpleEntityContainer = {
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

    container.storeEntity( controller, null )
    container.bindName( clientControllerName, controller )

    controller
  }

}


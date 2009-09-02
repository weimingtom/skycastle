package org.skycastle.client.rendering


import com.jme.app.{SimpleGame, BaseGame}
import com.jme.bounding.{BoundingSphere, BoundingBox}
import com.jme.scene.shape.Sphere
import com.jme.input.{KeyBindingManager, KeyInput}
import com.jme.math.Vector3f
import com.jme.image.Texture
import com.jme.renderer.ColorRGBA
import com.jme.scene.state.{ZBufferState, CullState}
import com.jme.scene.{Node, Skybox, Spatial}
import com.jme.system.{JmeException, DisplaySystem}
import com.jme.util.{TextureManager, Timer}
import org.skycastle.util.ResourceLoader

/**
 * Takes care of rendering the 3D view.
 *
 * @author Hans Haggstrom
 */
// TODO: Fuse the 2D Swing UI and the 3D JME UI - either include one into the other, or allow both to exists
// (some games / applications may prefer to just use normal swing ui).
class MainGameLoop( title: String, /*universe : Universe, */player : VisibleEntity ) extends BaseGame {

  //the root node of the scene graph
  private var scene : Node = null
  private var camera : CameraWrapper = null
  private var chaseCamera : CharacterChaseCamera = null
/*
  private var inputHandler : CharacterInputHandler = null
*/
  private var currentSpace : SpaceEntity = null


  def initSystem = {

    //store the properties information
    val width = settings.getWidth()
    val height = settings.getHeight()
    val depth = settings.getDepth()
    val freq = settings.getFrequency
    val fullscreen = settings.isFullscreen

    try {
      display = DisplaySystem.getDisplaySystem(settings.getRenderer())
      display.createWindow(width, height, depth, freq, fullscreen)
    } catch {
      case e: JmeException => {
        e.printStackTrace()
        System.exit(1)
      }
    }

    // Title
    display.setTitle(title)

    //set the background to black
    display.getRenderer().setBackgroundColor(ColorRGBA.black);

    //initialize the camera
    camera = new CameraWrapper( display, width, height )
    camera.setPosition( 700, 200, 700 )

    // TODO: Where do we listen for these 'commands'?
    KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
  }




  def initGame = {

    scene = new Node("Scene graph node")

/*
    currentSpace = player.getSpaceEntity
*/

    scene.attachChild( new SkySphere() )

/*
    scene.attachChild( currentSpace.getScene( display ) )
*/

/*
    chaseCamera = new CharacterChaseCamera( player.getSpatial( display ),
                                            3, */
/* player.getHeight * 1.5f, */
/*
                                            camera.camera )
*/

/*
    inputHandler = new CharacterInputHandler( player )
*/

    // Z buffering
    val buf = display.getRenderer().createZBufferState()
    buf.setEnabled( true )
    buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo )
    scene.setRenderState( buf )

    // Back culling
    val cs = display.getRenderer().createCullState()
    cs.setCullFace(CullState.Face.Back)
    scene.setRenderState(cs)

    //update the scene graph for rendering
    scene.updateGeometricState(0.0f, true);
    scene.updateRenderState();
  }

  def reinit = {
/*

    display.recreateWindow(width, height, depth, freq, fullscreen);
*/


  }

  def update(interpolate: Float) = {


/*
    universe.update

    val time = universe.timer.getTimePerFrame()
*/

    val time = interpolate

/*
    inputHandler.update( time )
*/

/*
    chaseCamera.update( time )
*/

    // if escape was pressed, we exit
    if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
       finished = true;
    }

/*
    //update the scene graph for rendering
    scene.updateGeometricState(0.0f, true);
    scene.updateRenderState();
*/

  }

  def render(interpolate: Float) = {

/*
    currentSpace.render
*/

    //Clear the screen
/*
    display.getRenderer().clearBuffers();

    display.getRenderer().draw(scene);
*/


  }

  def cleanup = {}


  def getDisplay = display



}
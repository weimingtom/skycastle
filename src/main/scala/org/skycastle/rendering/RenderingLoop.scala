package org.skycastle.rendering


import com.jme.app.BaseGame
import com.jme.input.{KeyBindingManager, KeyInput}
import com.jme.scene.state.{ZBufferState, CullState}
import com.jme.scene.{Spatial, Node}
import com.jme.system.{DisplaySystem, JmeException}
import com.jme.renderer.ColorRGBA

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class RenderingLoop(title : String) extends BaseGame {

  private var camera : CameraWrapper = null
  private var scene : Spatial = null



  def getDisplay = display

  def setScene( newScene : Spatial ) {
    scene = newScene

    initScene( scene )
    updateScene( scene )
  }

  def getScene : Spatial = scene


  def initSystem = {
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
        // TODO: Log error and return instead?
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

    // TODO: Initialize based on settings
    KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE)

  }


  def initGame = {

    // TODO: Make sure the scene is generated

    initScene(scene)

    updateScene( scene )
  }



  def update(p1: Float) = {


    // TODO: Handle according to settings
    // if escape was pressed, we exit
    if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
       finished = true;
    }

    updateScene( scene )
  }


  def render(p1: Float) = {
    // Clear the screen
    display.getRenderer().clearBuffers();

    // Draw scene
    if (scene != null) display.getRenderer().draw(scene);
  }



  def reinit = {
    // TODO: Handle resizing of the window
/*
    display.recreateWindow(width, height, depth, freq, fullscreen);
*/
  }


  def cleanup = {}


  private def initScene( node : Spatial ) {
    if (node != null && display != null) {
      // Z buffering
      val buf = display.getRenderer().createZBufferState()
      buf.setEnabled( true )
      buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo )
      node.setRenderState( buf )

      // Back culling
      val cs = display.getRenderer().createCullState()
      cs.setCullFace(CullState.Face.Back)
      node.setRenderState(cs)
    }
  }

  private def updateScene( node : Spatial ) {
    if (node != null && display != null) {
      node.updateGeometricState(0.0f, true);
      node.updateRenderState();
    }
  }

}


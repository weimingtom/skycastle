package org.skycastle.rendering

import org.skycastle.entity.Entity
import com.jme.app.AbstractGame
import com.jme.scene.Node
import com.jme.scene.shape.Sphere

/**
 * 
 */

class Screen3DEntity extends Entity {


  @transient
  private var renderingLoop : RenderingLoop = null

  

  def showScreen(  ) {
    if (renderingLoop == null) {
      val title = getString( 'title, "Skycastle" )
      renderingLoop = new RenderingLoop( title )

      // Generate scene and attach it to the rendering loop
      // TODO: Similar system for 3D scene as for UI:s?  - Possible to instantiate also without 3 representation, and used e.g. for collision detection and such?
      renderingLoop.setScene( new Sphere("testspehere", 16, 16, 20) )
      
      renderingLoop.setConfigShowMode( AbstractGame.ConfigShowMode.AlwaysShow )
      renderingLoop.start()
    }
    else {
      // Already running
    }
  }




}
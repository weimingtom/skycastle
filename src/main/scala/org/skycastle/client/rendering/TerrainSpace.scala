package org.skycastle.client.rendering


import _root_.org.skycastle.entity.EntityId
import util.ResourceLoader
import com.jme.bounding.{BoundingSphere, BoundingBox}
import com.jme.image.Texture
import com.jme.math.Vector3f
import com.jme.scene.{Node, Spatial}
import com.jme.util.TextureManager
import com.jmex.terrain.util.{ProceduralTextureGenerator, MidPointHeightMap}
import java.awt.Image
import javax.swing.ImageIcon
import com.jme.system.DisplaySystem

import com.jmex.terrain.TerrainBlock

/**
 * A Space that has a terrain as ground.
 *
 * @author Hans Haggstrom
 */

class TerrainSpace extends PhysicalSpace {

  private var terrain : TerrainBlock = null
  private var sky : SkySphere= null

  private val tempVec = new Vector3f()

  private var gravity = 9.8f

  def getHeightAt( location : Vector3f ) : Float = terrain.getHeightFromWorld( location )

  def getNormalAt( location : Vector3f, normalOut : Vector3f, sampleRadius : Float ) {

    def getHeight( xDelta : Float, zDelta : Float ) : Float = {
      tempVec.setX( location.x + xDelta )
      tempVec.setZ( location.z + zDelta )
      tempVec.setY( location.y )
      getHeightAt( tempVec )
    }

    val x0 = getHeight( -sampleRadius, 0 )
    val x1 = getHeight(  sampleRadius,  0 )
    val z0 = getHeight(  0, -sampleRadius )
    val z1 = getHeight(  0,  sampleRadius )

    normalOut.y = -sampleRadius
    normalOut.x = x0 - x1
    normalOut.z = z0 - z1
    normalOut.normalizeLocal

  }

  def getElasticityAt( location : Vector3f ) : Float = 0.2f


  override def render() {
    if (sky != null) sky.render()
  }

  private def generateTextureImage( heightMap : MidPointHeightMap ) : Image = {

    val grass = ResourceLoader.loadImage( "images/grassy_ground.jpg" )
    val sand = ResourceLoader.loadImage( "images/sandy_ground.jpg" )
    val stone = ResourceLoader.loadImage( "images/megastructure-logo.png" )

    // generate a terrain texture with 3 textures
    val pt = new ProceduralTextureGenerator(heightMap)
    pt.addTexture( new ImageIcon( grass ), -128, 0, 128)
    pt.addTexture( new ImageIcon( sand  ), 0, 128, 255)
    pt.addTexture( new ImageIcon( stone ), 128, 255, 384)
    pt.createTexture(256)

    pt.getImageIcon.getImage
  }


  override def update(timeSinceLastUpdate_s: Float) {
    super.update( timeSinceLastUpdate_s )

    sky.update()
  }

  override protected def createScene(display: DisplaySystem) : Node = {

/* toDO
    val node = new Node()

    terrain = createTerrain(display)
    sky = createSkyDome( node, terrain, display )

    node.attachChild( terrain )
    node.attachChild( sky )

    node.setRenderState( createLightState( display ) )

    getEntities foreach { entity : EntityId => node.attachChild( entity.getSpatial( display ) ) }

    node
*/
    null
  }


  private def createLightState(display: DisplaySystem) = {

    val lightState = display.getRenderer().createLightState()
    lightState.setEnabled(true)
    lightState.setTwoSidedLighting(true)

    lightState
  }

  /**
   * Initialize SkyDome
   */
  private def createSkyDome( scene : Node, lightTarget : Spatial, display: DisplaySystem ) : SkySphere = {
      val dome = new SkySphere("sky", new Vector3f(0.0f,0.0f,0.0f), 11, 18, 850.0f);
      dome.setModelBound(new BoundingSphere());
      dome.updateModelBound();
      dome.updateRenderState();
      dome.setUpdateTime(0.5f);
      dome.setTimeWarp(60.0f);
      dome.setDay(267);
      dome.setLatitude(-22.9f);
      dome.setLongitude(-47.083f);
      dome.setStandardMeridian(-45.0f);
      dome.setSunPosition(9f);             // 9 am
      dome.setTurbidity(2.0f);
      dome.setSunEnabled(true);
      dome.setExposure(true, 18.0f);
      dome.setOvercastFactor(0.0f);
      dome.setGammaCorrection(2.5f);
      dome.setRootNode(scene);
      dome.setIntensity(1.0f);

      // setup a target to LightNode, if you dont want terrain with light's effect remove it.
      dome.setTarget(scene, display);

      dome
  }

  private def createTerrain( display : DisplaySystem )  = {

    // Generate a random terrain data
    val heightMap = new MidPointHeightMap(64, 1.3f)

    // Scale the data
    val terrainScale = new Vector3f(20, 0.5f, 20)

    // create a terrainblock
    val terrain = new TerrainBlock("Terrain",
                          heightMap.getSize(),
                          terrainScale,
                          heightMap.getHeightMap(),
                          new Vector3f(0, 0, 0) )

    // Create texture
    val textureImage = generateTextureImage( heightMap )
    val ts = display.getRenderer().createTextureState()
    val t1 = TextureManager.loadTexture(textureImage,
                    Texture.MinificationFilter.BilinearNearestMipMap,
                    Texture.MagnificationFilter.Bilinear,
                    true)
    ts.setTexture(t1, 0)
    terrain.setRenderState(ts)

    terrain.setModelBound(new BoundingBox())
    terrain.updateModelBound()

    terrain
  }





  protected def checkCollision( entity : EntityId, position : Vector3f, velocity : Vector3f ) {

/*
    def getNormal = {
      getNormalAt( position, tempVec, entity.getRadius )
      tempVec
    }

    val groundHeight: Float = getHeightAt( position )
    if ( position.y < groundHeight  ) {
     position.y = groundHeight

      val elasticity = getElasticityAt( position )
      val impact = velocity.length * entity.getMass_kg
      val normal = getNormal

      entity.onCollision( position, normal, impact, elasticity )
    }
*/
  }

  protected def calculateForce(entity: EntityId, forceOut: Vector3f) {

/*
    forceOut.y = -gravity
*/

  }
}
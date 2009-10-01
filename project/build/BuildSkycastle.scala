
import sbt._


/**
 * SBT build script for Skycastle.
 * 
 * @author Hans Haggstrom
 */
class BuildSkycastle(info: ProjectInfo) extends DefaultProject(info)
{

  // Add some repositories we use
  val javaNetRepository = "java.net Maven2 Repository" at "http://download.java.net/maven/2/"

  //// Dependencies:

  // Testing
  val junitLib     = "junit"         % "junit"     % "4.4"
  val scalaTestLib = "org.scalatest" % "scalatest" % "0.9.5"

  // Darkstar Middleware
  val darkstarVersion = "0.9.10"
  val sgsServerApiLib = "com.projectdarkstar.server" % "sgs-server-api" % darkstarVersion
  val sgsClientApiLib = "com.projectdarkstar.client" % "sgs-client-api" % darkstarVersion
  val sgsClientLib    = "com.projectdarkstar.client" % "sgs-client"     % darkstarVersion
  val sgsSharedLib    = "com.projectdarkstar.server" % "sgs-shared"     % "1.8"

  // Darkstar client dependencies
  val slfVersion = "1.5.6"
  val slfApiLib = "org.slf4j" % "slf4j-api"   % slfVersion
  val slfJdkLib = "org.slf4j" % "slf4j-jdk14" % slfVersion
  val minaLib = "org.apache.mina" % "mina-core" % "1.1.7"

  // Java Monkey Engine for 3D graphics
  def makeJmeLib( artifact : String ) = "com.projectdarkstar.ext.com.jmonkeyengine" % artifact % "2.0-S1"
  val jmeLib        = makeJmeLib( "jme" )
  val jmeTerrainLib = makeJmeLib( "jme-terrain" )

  // JME Dependencies
  def makeLwjglLib( artifact : String ) = "com.projectdarkstar.ext.org.lwjgl" % artifact % "2.0rc2"
  val lwjglLib      = makeLwjglLib( "lwjgl" )
  val lwjglUtilLib  = makeLwjglLib( "lwjgl_util" )
  val jinputLib = "com.projectdarkstar.ext.net.java.dev.jinput" % "jinput" % "SNAPSHOT"
  val jorbisLib = "com.projectdarkstar.ext.jorbis"              % "jorbis" % "0.0.17"

  // Geometry
  val jtsLib = "com.vividsolutions" % "jts" % "1.8"

  // A custom layout manager
  val migLib = "com.miglayout" % "miglayout" % "3.6"


  // TODO: Add the following as jars directly in the lib folder, as they are not in maven repositories

  /*
  // Primitive collections
  val Lib = "trove" % "trove" % "2.0.4"
*/

/*
  // Tablet support
  val Lib = "jpen" % "jpen" % "2-081201"
*/

/*
  // Opening a webpage in a browser from Java
*/


}


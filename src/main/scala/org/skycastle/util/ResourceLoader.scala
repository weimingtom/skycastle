package org.skycastle.util

import _root_.scala.xml.Elem
import java.awt.image.BufferedImage
import java.io.{InputStreamReader, InputStream, Reader}
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 * Loads some resource that have been bundled with the Jar, using the classloader.
 *
 * @author Hans Haggstrom
 */

object ResourceLoader {
    def loadImage(resourcePath: String): BufferedImage = loadImage(resourcePath, null)

    def loadImage(resourcePath: String, placeholder: BufferedImage): BufferedImage = {
        loadResource[BufferedImage](resourcePath, "BufferedImage", ImageIO.read, placeholder)
    }

    def loadIcon(resourcePath: String): ImageIcon = {
        new ImageIcon(loadImage(resourcePath))
    }

    def loadProperties(resourcePath: String): java.util.Properties = {
        val properties = new java.util.Properties()

        loadResource[java.util.Properties](resourcePath, "Properties", (stream) => {
            properties.load(new InputStreamReader(stream))
            properties
        }, properties)
    }

    def loadXml(resourcePath: String, resourceDesc: String): Elem = {
        loadXml(resourcePath, resourceDesc, null)
    }

    def loadXml(resourcePath: String, resourceDesc: String, default: Elem): Elem = {
        loadResource[Elem](resourcePath, resourceDesc, scala.xml.XML.load, default)
    }

    def getResourceURL( resourcePath: String ) : URL = {

      try
      {
          val classloader: ClassLoader = ResourceLoader.getClass().getClassLoader()
          classloader.getResource( resourcePath )
      }
      catch
      {
          case e: Throwable => {
              // TODO: Use logging
              System.err.println("Failed to get the URL for the resource '" + resourcePath + "' " )
              e.printStackTrace

              return null
          }
      }

    }

    def loadResource[T](resourcePath: String, resourceDesc: String, loader: (InputStream) => T, default: => T): T = {
        // TODO: Could return a future maybe - requires a spawn type future support for scala (scalax.Future seems promising)
        // But then it should probably get the concrete default object.

        try
        {
            val classloader: ClassLoader = ResourceLoader.getClass().getClassLoader()

            if (resourcePath == null) default
            else {
              val stream: InputStream = classloader.getResourceAsStream(resourcePath)

              if (stream == null) default
              else loader(stream)
            }
        }
        catch
        {
            case e: Throwable => {
                val defaultVal = default

                // TODO: Use logging
                System.err.println("Failed to load the resource '" + resourceDesc + "' " +
                        "from the location '" + resourcePath + "' on the classpath" +
                        (if (defaultVal != null) " (using default value instead)" else "") + " : " + e.toString())
                e.printStackTrace

                return defaultVal
            }
        }
    }


}
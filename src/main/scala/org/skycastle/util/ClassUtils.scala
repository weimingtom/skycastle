package org.skycastle.util

import ParameterChecker._
/**
 * Utilities for dealing with classes.
 */
object ClassUtils {

  private val classUtilsLogger = new LogUtil("org.skycastle.util.ClassUtils")


  /**
   * Creates an object by instantiating the specified class with a no-parameters constructor.
   *
   * Throws an ObjectCreationException if the created class would not be of the specified kind.
   */
  @throws( classOf[ObjectCreationException] )
  def createObject[T]( classname : String, kind : Class[T] ) : T = {
    requireNotNull( classname, 'classname )
    requireNotNull( kind, 'kind )

    try {
      val typeClass : Class[_]= Class.forName( classname )
      if ( kind.isAssignableFrom( typeClass ) ) {
        try {
          typeClass.getConstructor().newInstance().asInstanceOf[T]
        }
        catch {
          case e  => throw new ObjectCreationException( "Problem when instantiating new object of type '"+classname+"' ", e )
        }
      }
      else {
        throw new ObjectCreationException( "The specified class type '"+classname+"' is not a subtype of '"+kind.getName+"' as required. ", null )
      }
    }
    catch {
      case e : ClassNotFoundException => throw new ObjectCreationException(  "No class named '"+classname+"' found", e )
    }
  }


  def preloadClasses( classes : Class[_] * ) {
    ParameterChecker.requireNotNull( classes, 'classes )

    classes foreach preloadClass
  }

  def preloadClass( clazz : Class[_] ) {
    ParameterChecker.requireNotNull( clazz, 'clazz )

    try {
      Class.forName( clazz.getName )
    }
    catch {
      case e =>
        classUtilsLogger.logWarning( "Could not preload class '"+clazz.getName+"', skipping it. ("+e.getMessage + ")" , e )
    }
  }
}



case class ObjectCreationException(message : String, cause : Throwable) extends Exception(message, cause)
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

  def getType( x : Any ) : Class[_] = x match  {
    case null       => classOf[AnyRef]
    case v : AnyRef => v.getClass
    case v : AnyVal => getValType( v )
  }

  def getValType(x: AnyVal): Class[_] = x match {
    case _: Byte => java.lang.Byte.TYPE
    case _: Short => java.lang.Short.TYPE
    case _: Int => java.lang.Integer.TYPE
    case _: Long => java.lang.Long.TYPE
    case _: Float => java.lang.Float.TYPE
    case _: Double => java.lang.Double.TYPE
    case _: Char => java.lang.Character.TYPE
    case _: Boolean => java.lang.Boolean.TYPE
    case _: Unit => java.lang.Void.TYPE
  }


  def toAnyRef( x: AnyVal ) : AnyRef = x match {
    case x: Byte => Byte.box(x)
    case x: Short => Short.box(x)
    case x: Int => Int.box(x)
    case x: Long => Long.box(x)
    case x: Float => Float.box(x)
    case x: Double => Double.box(x)
    case x: Char => Char.box(x)
    case x: Boolean => Boolean.box(x)
    case x: Unit => ()
  }

}



case class ObjectCreationException(message : String, cause : Throwable) extends Exception(message, cause)
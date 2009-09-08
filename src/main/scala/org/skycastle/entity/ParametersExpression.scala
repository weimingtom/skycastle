package org.skycastle.entity


import expression.Expression
import util.Parameters
import org.skycastle.util.Properties

/**
 * Contains expressions for creating a Parameters object based on an Entity.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
final case class ParametersExpression( sources : Map[Symbol, Expression] )  {

  def getParameters( properties : Properties ) : Parameters = {

    val params : Map[Symbol, Any] = Map() ++ ( sources map { case( parameter : Symbol, expression : Expression ) =>
      ( parameter, expression.getValue( properties ) )
    } )

    new Parameters( params )
  }

}

package org.skycastle.entity.expression

import _root_.junit.framework.TestCase
import entitycontainer.SimpleEntityContainer

import num._
import bool._
import org.junit._
import Assert._

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class ExpressionTest extends TestCase {


  def testExpressionCreation {

    val expr : Expression = Num(5) * Num(10) + NumProp( 'someValue, Num(0) ) < Num( 10) and Not( Prop( 'alarm, Const("none") ) sameAs Const("high"))

    expr.toString

  }

}
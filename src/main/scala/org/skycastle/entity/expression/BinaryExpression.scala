package org.skycastle.entity.expression

/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait BinaryExpression {

  val exp1 : Expression
  val exp2 : Expression
  val symbol : String

  override def toString = exp1.toString + " "+symbol+" " + exp2.toString

}




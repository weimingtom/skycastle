package org.skycastle.network

/**
 * Something that can be converted to types that the normal serializers can handle.
 *
 * Transferable implementations should have a companion object with a fromTransferObject( Object ) : Transferable -method,
 * which is used to convert transfered objects back to class instances. 
 * 
 * @author Hans Haggstrom
 */
trait Transferable {

  def decoderTypeName : String = this.getClass.getName + "$"

  def toTransferObject : Object

}
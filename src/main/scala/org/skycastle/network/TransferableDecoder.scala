package org.skycastle.network

/**
 * Something that can convert an encoded object back into a Transferable object.
 * 
 * @author Hans Haggstrom
 */

trait TransferableDecoder  {

  def fromTransferObject( transferedObject : Object ) : Transferable

}
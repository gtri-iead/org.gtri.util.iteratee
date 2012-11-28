package org.gtri.util.iteratee.impl

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api._


/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Translatees {
  case class Result[A,B](next : Translatee[A,B], output : Traversable[B], issues : Traversable[Issue] = Nil) extends Translatee.Result[A,B]

}

package org.gtri.util.iteratee.impl.translate

import org.gtri.util.iteratee.api.{StatusCode, Translatee}
import org.gtri.util.iteratee.impl.Translatees.Result

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
class IdentityTranslatee[A] extends Translatee[A,A] {

  def status() = StatusCode.CONTINUE

  def apply(items: Traversable[A]) = Result(this, items, Nil)
}

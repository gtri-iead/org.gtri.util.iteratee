package org.gtri.util.iteratee.impl.box

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/23/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
final class Lazy[+A](_value : => A) {
  lazy val value = _value
}
object Lazy {
  def apply[A](value : => A) = new Lazy(value)
}

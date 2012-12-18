package org.gtri.util.box

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/16/12
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
final class Recoverable[+A](lazyBox : => Box[A]) {
  lazy val recover = lazyBox
}

object Recoverable {
  def apply[A](lazyBox : => Box[A]) = new Recoverable[A](lazyBox)
}
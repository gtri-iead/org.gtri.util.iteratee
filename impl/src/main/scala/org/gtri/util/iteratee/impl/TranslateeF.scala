package org.gtri.util.iteratee.impl

import base.BaseTranslatee
import org.gtri.util.iteratee.api.Iteratee
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 6:16 AM
 * To change this template use File | Settings | File Templates.
 */
class TranslateeF[A,B,S](downstream : Iteratee[B,S], f: (A) => B) extends BaseTranslatee[A,B,S](downstream) {

  def apply(item: A) = TranslateeF(downstream(f(item)), f)

  def apply(eoi: EndOfInput) = TranslateeF(downstream(eoi), f)

  def attach[T](i: Iteratee[B, T]) = TranslateeF(i, f)
}
object TranslateeF {
  def apply[A,B,S](downstream : Iteratee[B,S], f: (A) => B) = new TranslateeF(downstream, f)
}
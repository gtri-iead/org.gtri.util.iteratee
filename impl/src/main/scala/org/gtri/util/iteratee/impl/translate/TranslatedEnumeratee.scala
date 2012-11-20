package org.gtri.util.iteratee.impl.translate

import org.gtri.util.iteratee.api.{Iteratee, Translatee, Enumeratee}
import org.gtri.util.iteratee.impl.base.BaseEnumeratee

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 2:21 AM
 * To change this template use File | Settings | File Templates.
 */
class TranslatedEnumeratee[A,B,S](val e : Enumeratee[A,S], t : Translatee[A,B,S]) extends BaseEnumeratee[B,S](t.downstream) {

  def isDone = e.isDone

  def attach[T](i: Iteratee[B, T]) = {
    val newT = t.attach(i)
    val newE = e.attach(newT)
    new TranslatedEnumeratee[A,B,T](newE,newT)
  }

  def step() = {
    val nextE = e.step()
    // TODO: find way to get rid of cast here
    // For now, the downstream of e should always be a translatee - if not throw an error here anyways
    val nextT = e.downstream().asInstanceOf[Translatee[A,B,S]]
    new TranslatedEnumeratee(nextE, nextT)
  }
}

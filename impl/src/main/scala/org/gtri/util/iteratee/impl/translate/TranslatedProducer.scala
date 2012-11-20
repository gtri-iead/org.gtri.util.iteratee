package org.gtri.util.iteratee.impl.translate

import org.gtri.util.iteratee.api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 2:25 AM
 * To change this template use File | Settings | File Templates.
 */
case class TranslatedProducer[A,B,S](producer : Producer[A], translator : Translator[A,B]) extends Producer[B] {

  def enumeratee[S](i: Iteratee[B,S]) = {
    val t = translator.translatee(i)
    val e = producer.enumeratee(t)
    new TranslatedEnumeratee(e,t)
  }
}

package org.gtri.util.iteratee.impl.translate

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api.Translator
import org.gtri.util.iteratee.impl.Translators._
import org.gtri.util.iteratee.impl.Translators

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
class IdentityTranslator[A] extends Translator[A,A] {

  class Cont extends Translators.Cont[A,A] {
    def apply(items: Traversable[A]) = Result(this, items, Nil)

    def endOfInput() = Result(Success())
  }

  def initialState() = new Cont
}

package org.gtri.util.iteratee.impl.test

import org.gtri.util.iteratee.api.Iteratee
import org.gtri.util.iteratee.impl.Iteratees._
import org.gtri.util.iteratee.impl.Iteratees
import collection.immutable.Traversable

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/28/12
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
class TestIntIteratee extends Iteratee[java.lang.Integer, java.lang.Integer] {
  class Cont(loopState : java.lang.Integer) extends Iteratees.Cont[java.lang.Integer, java.lang.Integer](loopState) {
    def apply(items: Traversable[java.lang.Integer]) = {
      println("items=" + items + " loopState=" + loopState)
      Result(new Cont(items.fold(loopState) { _ + _ }))
    }

    def endOfInput() = Result(Success(loopState))
  }
  def initialState() = new Cont(0)
}


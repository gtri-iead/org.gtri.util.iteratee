package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api._
import scala.collection.immutable.Traversable


/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/28/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
object BuilderStates {
  case class Result[A,V](next : Builder.State[A,V], overflow : Traversable[A] = Nil, issues : Traversable[Issue] = Nil) extends Machine.State.Result[A,Option[V],Builder.State[A,V]] {
    def output = next.value
  }
//  type IterV[A,V] = Iteratee[A, Option[V]]
//  object IterV {
  abstract class BaseCont[A,V] extends Builder.State[A,V] {

    def value = None
  }

  abstract class Cont[A,V] extends BaseCont[A,V] {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A,V] extends BaseCont[A,V] {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  abstract class BaseDone[A,V](val value : Option[V]) extends Builder.State[A,V] {
    def endOfInput = Result(this)
  }

  class Success[A,V](value : Option[V]) extends BaseDone[A,V](value) {
    def status = StatusCode.SUCCESS

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object Success {
    def apply[A,V](value : Option[V]) = new Success[A,V](value)
  }
  class FatalError[A,V](value : Option[V]) extends BaseDone[A,V](value) {
    def status = StatusCode.FATAL_ERROR

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object FatalError {
    def apply[A,V](value : Option[V]) = new FatalError[A,V](value)
  }
//  }


}

package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api._
import scala.collection.immutable.Traversable


/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/28/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
object Consumers {
  case class Result[A](next : Consumer.State[A], overflow : Traversable[A] = Nil, issues : Traversable[Issue] = Nil) extends Machine.State.Result[A,Unit,Consumer.State[A]] {
    def output() {}
  }
  //  type IterS[A] = Iteratee[A, Unit]
//  object IterS {
  abstract class BaseCont[A] extends Consumer.State[A] {
  }

  abstract class Cont[A] extends BaseCont[A] {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A] extends BaseCont[A] {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  abstract class BaseDone[A] extends Consumer.State[A] {
    def endOfInput = Result(this)
  }

  class Success[A] extends BaseDone[A] {
    def status = StatusCode.SUCCESS

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object Success {
    def apply[A]() = new Success[A]()
  }
  class FatalError[A] extends BaseDone[A] {
    def status = StatusCode.FATAL_ERROR

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object FatalError {
    def apply[A]() = new FatalError[A]()
  }

}

package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
object BaseIterV {
  object base {
    abstract class BaseCont[A,V](val issues : List[Issue] = Nil) extends IterV[A,V] {
      def isDone = false

      def state = this

      def overflow = Nil

      def value = None
    }

    abstract class Cont[A,V](issues : List[Issue] = Nil) extends BaseCont[A,V](issues) {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A,V](issues : List[Issue] = Nil) extends BaseCont[A,V](issues) {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A,V](val issues : List[Issue] = Nil, val overflow : List[A]) extends IterV[A,V] {
      def isDone = true

      def state = this

      def apply(ignore: EndOfInput) = this
    }

    class Success[A,V](val value : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,V](issues, overflow) {
      def status() = StatusCode.SUCCESS

      def apply(item: A) = Success(value, issues, item :: overflow)
    }
    object Success {
      def apply[A,V](value : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) = new Success(value, issues, overflow)
    }
    class FatalError[A,V](issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,V](issues, overflow) {
      def status() = StatusCode.FATAL_ERROR

      def apply(item: A) = FatalError[A,V](issues, item :: overflow)

      def value() = None
    }
    object FatalError {
      def apply[A,V](issues : List[Issue] = Nil, overflow : List[A] = Nil) = new FatalError[A,V](issues, overflow)
    }
  }
}

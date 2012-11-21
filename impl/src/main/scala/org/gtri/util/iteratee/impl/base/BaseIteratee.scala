package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api.{StatusCode, Iteratee, Issue}
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 6:37 AM
 * To change this template use File | Settings | File Templates.
 */
class BaseIteratee {
  object base {
    abstract class BaseCont[A,S](val state : S, val issues : List[Issue] = Nil) extends Iteratee[A,S] {
      def isDone = false

      def overflow = Nil
    }

    abstract class Cont[A,S](state : S, issues : List[Issue] = Nil) extends BaseCont[A,S](state, issues) {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A,S](state : S, issues : List[Issue] = Nil) extends BaseCont[A,S](state, issues) {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A,S](val state : S, val issues : List[Issue] = Nil, val overflow : List[A]) extends Iteratee[A,S] {
      def isDone = true

      def apply(ignore: EndOfInput) = this
    }

    class Success[A,S](state : S, issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,S](state, issues, overflow) {
      def status = StatusCode.SUCCESS

      def apply(item: A) = Success(state, issues, item :: overflow)
    }
    object Success {
      def apply[A,S](state : S, issues : List[Issue] = Nil, overflow : List[A] = Nil) = new Success(state, issues, overflow)
    }
    class FatalError[A,S](state : S, issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,S](state, issues, overflow) {
      def status = StatusCode.FATAL_ERROR

      def apply(item: A) = FatalError(state, issues, item :: overflow)
    }
    object FatalError {
      def apply[A,S](state : S, issues : List[Issue] = Nil, overflow : List[A] = Nil) = new FatalError[A,S](state, issues, overflow)
    }
  }

}

package org.gtri.util.iteratee.impl

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api
import api.{Issue, StatusCode, Iteratee}

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
object Iteratees {
  case class Result[A,S](next : api.Iteratee[A,S], issues : Traversable[api.Issue] = Nil, overflow : Traversable[A] = Nil) extends api.Iteratee.Result[A,S]

  abstract class BaseCont[A,S](val state : S) extends Iteratee[A,S]

  abstract class Cont[A,S](state : S) extends BaseCont[A,S](state) {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A,S](state : S) extends BaseCont[A,S](state) {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  abstract class BaseDone[A,S](val state : S) extends Iteratee[A,S] {
    def endOfInput = Result(this)
  }

  class Success[A,S](state : S) extends BaseDone[A,S](state) {
    def status = StatusCode.SUCCESS

    def apply(items: Traversable[A]) = Result(Success(state), Nil, items)
  }
  object Success {
    def apply[A,S](state : S) = new Success[A,S](state)
  }
  class FatalError[A,S](state : S) extends BaseDone[A,S](state) {
    def status = StatusCode.FATAL_ERROR

    def apply(items: Traversable[A]) = Result(FatalError(state), Nil, items)
  }
  object FatalError {
    def apply[A,S](state : S) = new FatalError[A,S](state)
  }


  type IterV[A,V] = Iteratee[A, Option[V]]
  object IterV {
    abstract class BaseCont[A,V] extends IterV[A,V] {
      def state = None
    }

    abstract class Cont[A,V] extends BaseCont[A,V] {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A,V] extends BaseCont[A,V] {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A,V](val state : Option[V]) extends IterV[A,V] {
      def endOfInput = Result(this)
    }

    class Success[A,V](state : Option[V]) extends BaseDone[A,V](state) {
      def status = StatusCode.SUCCESS

      def apply(items: Traversable[A]) = Result(Success(state), Nil, items)
    }
    object Success {
      def apply[A,V](state : Option[V]) = new Success[A,V](state)
    }
    class FatalError[A,V](state : Option[V]) extends BaseDone[A,V](state) {
      def status = StatusCode.FATAL_ERROR

      def apply(items: Traversable[A]) = Result(FatalError(state), Nil, items)
    }
    object FatalError {
      def apply[A,V](state : Option[V]) = new FatalError[A,V](state)
    }
  }

  type IterS[A] = Iteratee[A, Unit]
  object IterS {
    abstract class BaseCont[A] extends IterS[A] {
      def state = ()
    }

    abstract class Cont[A] extends BaseCont[A] {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A] extends BaseCont[A] {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A] extends IterS[A] {
      def state = ()

      def endOfInput = Result(this)
    }

    class Success[A] extends BaseDone[A] {
      def status = StatusCode.SUCCESS

      def apply(items: Traversable[A]) = Result(Success(), Nil, items)
    }
    object Success {
      def apply[A]() = new Success[A]
    }
    class FatalError[A] extends BaseDone[A] {
      def status = StatusCode.FATAL_ERROR

      def apply(items: Traversable[A]) = Result(FatalError(), Nil, items)
    }
    object FatalError {
      def apply[A]() = new FatalError[A]
    }
  }
}

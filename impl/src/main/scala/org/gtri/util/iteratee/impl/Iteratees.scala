package org.gtri.util.iteratee.impl

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api
import api._

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/26/12
* Time: 5:20 PM
* To change this template use File | Settings | File Templates.
*/
object Iteratees {
  case class Result[A,S](next : IterateeState[A,S], overflow : Traversable[A] = Nil, issues : Traversable[Issue] = Nil) extends MachineState.Result[A,S,IterateeState[A,S]] {
    def output() = next.loopState
  }

  abstract class BaseCont[A,S](val loopState : S) extends IterateeState[A,S]

  abstract class Cont[A,S](loopState : S) extends BaseCont[A,S](loopState) {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A,S](loopState : S) extends BaseCont[A,S](loopState) {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  abstract class BaseDone[A,S](val loopState : S) extends IterateeState[A,S] {
    def endOfInput = Result(this)
  }

  class Success[A,S](loopState : S) extends BaseDone[A,S](loopState) {
    def status = StatusCode.SUCCESS

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object Success {
    def apply[A,S](loopState : S) = new Success[A,S](loopState)
  }
  class FatalError[A,S](loopState : S) extends BaseDone[A,S](loopState) {
    def status = StatusCode.FATAL_ERROR

    def apply(items: Traversable[A]) = Result(this, items)
  }
  object FatalError {
    def apply[A,S](loopState : S) = new FatalError[A,S](loopState)
  }


}

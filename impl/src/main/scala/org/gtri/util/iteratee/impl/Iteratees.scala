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
  case class Result[I,O](next : Iteratee.State[I,O], output : Traversable[O] = Traversable.empty, overflow : Traversable[I] = Nil, issues : Traversable[Issue] = Nil) extends Iteratee.State.Result[I,O]

  abstract class Cont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE
  }

  abstract class RecoverableError[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.RECOVERABLE_ERROR
  }

  class Success[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.SUCCESS

    def apply(items: Traversable[I]) = Result(this, Nil, items)

    def endOfInput() = Result(this, Nil, Nil)
  }

  object Success {
    def apply[I,O]() = new Success[I,O]
  }

  class FatalError[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.FATAL_ERROR

    def apply(items: Traversable[I]) = Result(this, Nil, items)

    def endOfInput() = Result(this, Nil, Nil)
  }
  object FatalError {
    def apply[I,O]() = new FatalError[I,O]()
  }


}

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
  case class Result[I,O](next : Iteratee.State[I,O], output : ImmutableBuffer[O] = ImmutableBuffers.empty[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]) extends Iteratee.State.Result[I,O]

  abstract class Cont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE
  }

  abstract class RecoverableError[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.RECOVERABLE_ERROR
  }

  class Success[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.SUCCESS

    def apply(items: ImmutableBuffer[I]) = Result(this, ImmutableBuffers.empty, items)

    def endOfInput() = Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty)
  }

  object Success {
    def apply[I,O]() = new Success[I,O]
  }

  class FatalError[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.FATAL_ERROR

    def apply(items: ImmutableBuffer[I]) = Result(this, ImmutableBuffers.empty, items)

    def endOfInput() = Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty)
  }
  object FatalError {
    def apply[I,O]() = new FatalError[I,O]()
  }


}

package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import api._
import scala.collection.immutable.Traversable

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 3:59 AM
 * To change this template use File | Settings | File Templates.
 */
object Plan2 {
  case class Result[I,O](next : State[I,O], output : Traversable[O], overflow : Traversable[I], issues : Traversable[Issue]) extends api.Plan2.State.Result[I,O]

  case class State[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O], val statusCode : StatusCode) extends api.Plan2.State[I,O] {

    def progress = enumeratorState.progress

    def step() = {
      val eResult = enumeratorState.step()
      val iResult = iterateeState.apply(eResult.output)
      val nextStatus = StatusCode.and(eResult.next.statusCode, iResult.next.statusCode)
      val nextState = State(eResult.next(), iResult.next, nextStatus)
      Result(nextState, iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
    }
  }
  object State {
    def apply[I,O](enumeratorState : Enumerator.State[I], iterateeState : Iteratee.State[I,O]) = {
      new State[I,O](enumeratorState, iterateeState, StatusCode.and(enumeratorState.statusCode, iterateeState.statusCode))
    }
  }
}
class Plan2[I,O](val factory : IterateeFactory, val enumerator : Enumerator[I], val iteratee : Iteratee[I,O]) extends api.Plan2[I,O] {
  def initialState = Plan2.State(enumerator.initialState, iteratee.initialState)

  def run() = {
    val (result, _allOutput, _allIssues) = Enumerators.run[O,Plan2.Result[I,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
    val iResult_EOI = result.next.iterateeState.endOfInput()
    new api.Plan2.RunResult[I,O] {

      def progress = result.next.progress

      def statusCode = StatusCode.and(result.next.enumeratorState.statusCode,iResult_EOI.next.statusCode)

      def overflow = iResult_EOI.overflow

      def enumerator = factory.createEnumerator(result.next.enumeratorState)

      def iteratee = factory.createIteratee(result.next.iterateeState)

      def allOutput = iResult_EOI.output :: _allOutput

      def allIssues = iResult_EOI.issues ++ _allIssues
    }
  }
}

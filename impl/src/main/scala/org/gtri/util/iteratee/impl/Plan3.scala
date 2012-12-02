package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import api._
import scala.collection.immutable.Traversable

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
object Plan3 {
  case class Result[I1,I2,O](next : State[I1,I2,O], output : Traversable[O], overflow : Traversable[I2], issues : Traversable[Issue]) extends api.Plan3.State.Result[I1,I2,O]

  case class State[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O], val status : Status) extends api.Plan3.State[I1,I2,O] {
    def step() = {
      val eResult = enumeratorState.step()
      val tResult = translatorState.apply(eResult.output)
      val iResult = iterateeState.apply(tResult.output)
      val nextStatus = Status.and(eResult.next.status, tResult.next.status, iResult.next.status)
      val nextState = State(eResult.next, tResult.next, iResult.next, nextStatus)
      Result[I1,I2,O](nextState, iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
    }
  }
  object State {
    def apply[I1,I2,O](enumeratorState : Enumerator.State[I1], translatorState : Iteratee.State[I1,I2], iterateeState : Iteratee.State[I2,O]) = {
      new State[I1,I2,O](enumeratorState, translatorState, iterateeState, Status.and(enumeratorState.status, translatorState.status, iterateeState.status))
    }
  }
}
class Plan3[I1,I2,O](val factory: IterateeFactory, val enumerator : Enumerator[I1], val translator : Iteratee[I1,I2], val iteratee : Iteratee[I2,O]) extends api.Plan3[I1,I2,O] {
  def initialState = Plan3.State(enumerator.initialState, translator.initialState, iteratee.initialState)

  def run() = {
    val (result, _allOutput,_allIssues) = Enumerators.run[O, Plan3.Result[I1,I2,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
    val tResult_EOI = result.next.translatorState.endOfInput()
    val iResult = result.next.iterateeState.apply(tResult_EOI.output())
    val iResult_EOI = iResult.next.endOfInput()
    new api.Plan3.RunResult[I1,I2,O] {

      def status = Status.and(result.next.enumeratorState.status,tResult_EOI.next.status, iResult_EOI.next.status)

      def allOutput = iResult_EOI.output :: iResult.output :: _allOutput

      def allIssues = tResult_EOI.issues ++ iResult.issues ++ iResult_EOI.issues ++ _allIssues

      def overflow = iResult_EOI.overflow

      def enumerator = factory.createEnumerator(result.next.enumeratorState)

      def translator = factory.createIteratee(result.next.translatorState)

      def iteratee = factory.createIteratee(result.next.iterateeState)
    }
  }
}

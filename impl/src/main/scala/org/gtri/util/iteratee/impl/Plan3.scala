/*
    Copyright 2012 Georgia Tech Research Institute

    Author: lance.gatlin@gtri.gatech.edu

    This file is part of org.gtri.util.iteratee library.

    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.

*/

package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
object Plan3 {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  case class Result[I1,I2,O](next : State[I1,I2,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I2], issues : ImmutableBuffer[Issue]) extends api.Plan3.State.Result[I1,I2,O]

  case class State[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O], val statusCode : StatusCode) extends api.Plan3.State[I1,I2,O] {

    def progress = enumeratorState.progress

    def step() = {
      val eResult = enumeratorState.step()
      val tResult = translatorState.apply(eResult.output)
      val iResult = iterateeState.apply(tResult.output)
      val nextStatus = StatusCode.and(eResult.next.statusCode, tResult.next.statusCode, iResult.next.statusCode)
      val nextState = State(eResult.next, tResult.next, iResult.next, nextStatus)
      Result[I1,I2,O](nextState, iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
    }
  }
  object State {
    def apply[I1,I2,O](enumeratorState : Enumerator.State[I1], translatorState : Iteratee.State[I1,I2], iterateeState : Iteratee.State[I2,O]) = {
      new State[I1,I2,O](enumeratorState, translatorState, iterateeState, StatusCode.and(enumeratorState.statusCode, translatorState.statusCode, iterateeState.statusCode))
    }
  }
}
class Plan3[I1,I2,O](val factory: IterateeFactory, val enumerator : Enumerator[I1], val translator : Iteratee[I1,I2], val iteratee : Iteratee[I2,O]) extends api.Plan3[I1,I2,O] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  def initialState = Plan3.State(enumerator.initialState, translator.initialState, iteratee.initialState)

  def run() = {
    val (lastResult, _allOutput,_allIssues) = Enumerators.runFlatten[O, Plan3.Result[I1,I2,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
    val tResult_EOI = lastResult.next.translatorState.endOfInput()
    val iResult = lastResult.next.iterateeState.apply(tResult_EOI.output())
    val iResult_EOI = iResult.next.endOfInput()
    new api.Plan3.RunResult[I1,I2,O] {

      def progress = lastResult.next.progress

      def statusCode = StatusCode.and(lastResult.next.enumeratorState.statusCode,tResult_EOI.next.statusCode, iResult_EOI.next.statusCode)

      def allOutput = iResult_EOI.output ++ iResult.output ++ _allOutput

      def allIssues = tResult_EOI.issues ++ iResult.issues ++ iResult_EOI.issues ++ _allIssues

      def overflow = iResult_EOI.overflow

      def enumerator = factory.createEnumerator(lastResult.next.enumeratorState)

      def translator = factory.createIteratee(lastResult.next.translatorState)

      def iteratee = factory.createIteratee(lastResult.next.iterateeState)
    }
  }
}

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
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.Enumerators.EnumeratorIterator


/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
class Plan3[I1,I2,O](val factory: IterateeFactory, val enumerator : Enumerator[I1], val translator : Iteratee[I1,I2], val iteratee : Iteratee[I2,O]) extends api.Plan3[I1,I2,O] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  def initialState = State(enumerator.initialState, translator.initialState, iteratee.initialState)

  case class Result[I1,I2,O](next : api.Plan3.State[I1,I2,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I2], issues : ImmutableBuffer[Issue]) extends api.Plan3.State.Result[I1,I2,O]

  case class State[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O]) extends api.Plan3.State[I1,I2,O] {
    def statusCode = StatusCode.or(enumeratorState.statusCode, translatorState.statusCode, iterateeState.statusCode)

    def progress = enumeratorState.progress

    def endOfInput() = {
      val tResult_EOI = translatorState.endOfInput()
      val iResult = iterateeState.apply(tResult_EOI.output())
      val iResult_EOI = iResult.next.endOfInput()
      Result(State(enumeratorState, tResult_EOI.next, iResult_EOI.next), iResult.output, iResult.overflow, iResult.issues)
    }

    def step() = {
      if(statusCode.isDone) {
        Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
      } else {
        val eResult = enumeratorState.step()
        val tResult = translatorState.apply(eResult.output)
        val iResult = iterateeState.apply(tResult.output)
        Result(State(eResult.next, tResult.next, iResult.next), iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
      }
    }
  }

  def iterator : java.util.Iterator[Plan3.State.Result[I1,I2,O]] = new EnumeratorIterator[O,Plan3.State.Result[I1,I2,O]](initialState.step(), { _.next.step() })

  def run() = {
    val initialR = initialState.step()
    val i : Iterator[Plan3.State.Result[I1,I2,O]] = new EnumeratorIterator[O,Plan3.State.Result[I1,I2,O]](initialR, { _.next.step() })
    val (_lastResult, _allOutput, _allIssues) = {
      i.foldLeft[(Plan3.State.Result[I1,I2,O], IndexedSeq[O],IndexedSeq[Issue])]((initialR, IndexedSeq[O](), IndexedSeq[Issue]())) {
        (accTuple, result) =>
          val (_, outputAcc, issuesAcc) = accTuple
          (result, result.output ++ outputAcc, result.issues ++ issuesAcc)
      }
    }
    val eoiResult = _lastResult.next.endOfInput()
    new api.Plan3.RunResult[I1,I2,O] {
      def lastResult = _lastResult

      def endOfInput = eoiResult

      def statusCode = eoiResult.next.statusCode

      def progress = eoiResult.next.progress

      def enumerator = factory.createEnumerator(_lastResult.next.enumeratorState)

      def translator = factory.createIteratee(_lastResult.next.translatorState)

      def iteratee = factory.createIteratee(_lastResult.next.iterateeState)

      def allOutput = eoiResult.output ++ _allOutput

      def allIssues = eoiResult.issues ++ _allIssues
    }
  }
}

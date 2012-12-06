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
 * Time: 3:59 AM
 * To change this template use File | Settings | File Templates.
 */
class Plan2[I,O](val factory : IterateeFactory, val enumerator : Enumerator[I], val iteratee : Iteratee[I,O]) extends api.Plan2[I,O] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  def initialState = new State(enumerator.initialState, iteratee.initialState)

  case class Result[I,O](next : api.Plan2.State[I,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) extends api.Plan2.State.Result[I,O]

  case class State[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O]) extends api.Plan2.State[I,O] {
    def statusCode = StatusCode.or(enumeratorState.statusCode, iterateeState.statusCode)

    def progress = enumeratorState.progress

    def endOfInput() = {
      val iResult_EOI = iterateeState.endOfInput()
      Result(State(enumeratorState, iResult_EOI.next), iResult_EOI.output, iResult_EOI.overflow, iResult_EOI.issues)
    }

    def step() = {
      if (statusCode.isDone) {
        Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
      } else {
        val eResult = enumeratorState.step()
        val iResult = iterateeState.apply(eResult.output)
        Result(State(eResult.next, iResult.next), iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
      }
    }
  }

  def iterator : java.util.Iterator[Plan2.State.Result[I,O]] = new EnumeratorIterator[O,Plan2.State.Result[I,O]](initialState.step(), { _.next.step() })

  def run() = {
    val initialR = initialState.step()
    val i : Iterator[Plan2.State.Result[I,O]] = new EnumeratorIterator[O,Plan2.State.Result[I,O]](initialR, { _.next.step() })
    val (_lastResult, _allOutput, _allIssues) = {
      i.foldLeft[(Plan2.State.Result[I,O], IndexedSeq[O],IndexedSeq[Issue])]((initialR, IndexedSeq[O](), IndexedSeq[Issue]())) {
        (accTuple, result) =>
          val (_, outputAcc, issuesAcc) = accTuple
          (result, result.output ++ outputAcc, result.issues ++ issuesAcc)
      }
    }
    val eoiResult = _lastResult.next.endOfInput()
    new api.Plan2.RunResult[I,O] {

      def lastResult = _lastResult

      def endOfInput = eoiResult

      def statusCode = eoiResult.next.statusCode

      def progress = eoiResult.next.progress

      def enumerator = factory.createEnumerator(_lastResult.next.enumeratorState)

      def iteratee = factory.createIteratee(_lastResult.next.iterateeState)

      def allOutput = eoiResult.output ++ _allOutput

      def allIssues = eoiResult.issues ++ _allIssues
    }
  }
}

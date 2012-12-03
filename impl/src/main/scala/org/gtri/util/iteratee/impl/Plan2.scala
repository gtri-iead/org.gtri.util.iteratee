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

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 3:59 AM
 * To change this template use File | Settings | File Templates.
 */
class Plan2[I,O](val factory : IterateeFactory, val enumerator : Enumerator[I], val iteratee : Iteratee[I,O]) extends api.Plan2[I,O] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  def initialState = new Cont(enumerator.initialState, iteratee.initialState)

  case class Result[I,O](next : api.Plan2.State[I,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) extends api.Plan2.State.Result[I,O]

  abstract class BaseState[I,O](enumeratorState : Enumerator.State[I], iterateeState : Iteratee.State[I,O]) extends api.Plan2.State[I,O] {
    val statusCode = StatusCode.and(enumeratorState.statusCode, iterateeState.statusCode)

    def progress = enumeratorState.progress

    def endOfInput() = {
      val iResult_EOI = iterateeState.endOfInput()
      Result(Done(enumeratorState, iResult_EOI.next), iResult_EOI.output, iResult_EOI.overflow, iResult_EOI.issues)
    }
  }

  // Enumerator is done and EOI fed to iteratee OR iteratee is DONE
  case class Done[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O]) extends BaseState[I,O](enumeratorState, iterateeState) {

    def step() = Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
  }

  // Enumerator is done, step causes EOI to be fed to iteratee
  case class EOI[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O]) extends BaseState[I,O](enumeratorState, iterateeState) {
    def statusCodes = iterateeState.statusCode

    def step() = {
      endOfInput()
    }
  }

  case class Cont[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O]) extends BaseState[I,O](enumeratorState, iterateeState) {

    def step() = {
      val eResult = enumeratorState.step()
      val iResult = iterateeState.apply(eResult.output)
      val nextState = {
        iResult.next.statusCode match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR =>
            eResult.next.statusCode match {
              case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => Cont(eResult.next, iResult.next)
              case StatusCode.FATAL_ERROR => Done(eResult.next, iResult.next)
              case StatusCode.SUCCESS => EOI(eResult.next, iResult.next)
            }
          case StatusCode.FATAL_ERROR => Done(eResult.next, iResult.next)
          case StatusCode.SUCCESS => Done(eResult.next, iResult.next)
        }
      }
      Result(nextState, iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
    }
  }

//  def runFoldLeft[U](u: U, f: Function2[U, Plan2.State.Result[I, O], U]) = {
//    val iterator : Iterator[Plan2.State.Result[I,O]] = Enumerators.iterator[O,Plan2.State.Result[I,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
//    iterator.foldLeft(u) {
//      (acc, result) =>
//        f(acc, result)
//    }
//  }
//
//  def runForEach(f: Function1[Plan2.State.Result[I, O], _]) = {
//    val iterator : Iterator[Plan2.State.Result[I,O]] = Enumerators.iterator[O,Plan2.State.Result[I,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
//    iterator.foreach {
//      f(_)
//    }
//  }


  def iterator : java.util.Iterator[Plan2.State.Result[I,O]] = Enumerators.iterator[O,Plan2.State.Result[I,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })

  def iterator(issueHandlingCode: IssueHandlingCode) : java.util.Iterator[Plan2.State.Result[I,O]] = Enumerators.iterator[O,Plan2.State.Result[I,O]](issueHandlingCode, initialState.step(), { _.next.step() })

  def run() = {
    val initialR = initialState.step()
    val iterator : Iterator[Plan2.State.Result[I,O]] = Enumerators.iterator[O,Plan2.State.Result[I,O]](factory.issueHandlingCode, initialR, { _.next.step() })
    val (_lastResult, _allOutput, _allIssues) = {
      iterator.foldLeft[(Plan2.State.Result[I,O], IndexedSeq[O],IndexedSeq[Issue])]((initialR, IndexedSeq[O](), IndexedSeq[Issue]())) {
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

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
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
class Plan3[I1,I2,O](val factory: IterateeFactory, val enumerator : Enumerator[I1], val translator : Iteratee[I1,I2], val iteratee : Iteratee[I2,O]) extends api.Plan3[I1,I2,O] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

  def initialState = Cont(enumerator.initialState, translator.initialState, iteratee.initialState)

  case class Result[I1,I2,O](next : api.Plan3.State[I1,I2,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I2], issues : ImmutableBuffer[Issue]) extends api.Plan3.State.Result[I1,I2,O]

  abstract class BaseState[I1,I2,O](enumeratorState : Enumerator.State[I1], translatorState : Iteratee.State[I1,I2], iterateeState : Iteratee.State[I2,O]) extends api.Plan3.State[I1,I2,O] {
    val statusCode = StatusCode.and(enumeratorState.statusCode, translatorState.statusCode, iterateeState.statusCode)

    def progress = enumeratorState.progress

    def endOfInput() = {
      val tResult_EOI = translatorState.endOfInput()
      val iResult = iterateeState.apply(tResult_EOI.output())
      val iResult_EOI = iResult.next.endOfInput()
      Result(Done(enumeratorState, tResult_EOI.next, iResult_EOI.next), iResult.output, iResult.overflow, iResult.issues)
    }
  }

  // Enumerator is done and EOI fed to iteratee OR iteratee is DONE
  case class Done[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O]) extends BaseState[I1,I2,O](enumeratorState, translatorState, iterateeState) {

    def step() = Result(this, ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
  }

  // Enumerator is done, step causes EOI to be fed to iteratee
  case class EOI[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O]) extends BaseState[I1,I2,O](enumeratorState, translatorState, iterateeState) {
    def statusCodes = iterateeState.statusCode

    def step() = {
      endOfInput()
    }
  }

  case class Cont[I1,I2,O](val enumeratorState : Enumerator.State[I1], val translatorState : Iteratee.State[I1,I2], val iterateeState : Iteratee.State[I2,O]) extends BaseState[I1,I2,O](enumeratorState, translatorState, iterateeState) {

    def step() = {
      val eResult = enumeratorState.step()
      val tResult = translatorState.apply(eResult.output)
      val iResult = iterateeState.apply(tResult.output)
      val itStatus = StatusCode.and(tResult.next.statusCode, iResult.next.statusCode)
      val nextState = {
         itStatus match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR =>
            eResult.next.statusCode match {
              case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => Cont(eResult.next, tResult.next, iResult.next)
              case StatusCode.FATAL_ERROR => Done(eResult.next, tResult.next, iResult.next)
              case StatusCode.SUCCESS => EOI(eResult.next, tResult.next, iResult.next)
            }
          case StatusCode.FATAL_ERROR => Done(eResult.next, tResult.next, iResult.next)
          case StatusCode.SUCCESS => Done(eResult.next, tResult.next, iResult.next)
        }
      }
      Result[I1,I2,O](nextState, iResult.output, iResult.overflow, eResult.issues ++ iResult.issues)
    }
  }

//  def runFoldLeft[U](u: U, f: Function2[U, Plan3.State.Result[I1,I2, O], U]) = {
//    val iterator : Iterator[Plan3.State.Result[I1,I2,O]] = Enumerators.iterator[O,Plan3.State.Result[I1,I2,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
//    iterator.foldLeft(u) {
//      (acc, result) =>
//        f(acc, result)
//    }
//  }
//
//  def runForEach(f: Function1[Plan3.State.Result[I1,I2,O], _]) = {
//    val iterator : Iterator[Plan3.State.Result[I1,I2,O]] = Enumerators.iterator[O,Plan3.State.Result[I1,I2,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })
//    iterator.foreach {
//      f(_)
//    }
//  }

  def iterator : java.util.Iterator[Plan3.State.Result[I1,I2,O]] = Enumerators.iterator[O,Plan3.State.Result[I1,I2,O]](factory.issueHandlingCode, initialState.step(), { _.next.step() })

  def iterator(issueHandlingCode: IssueHandlingCode) : java.util.Iterator[Plan3.State.Result[I1,I2,O]] = Enumerators.iterator[O,Plan3.State.Result[I1,I2,O]](issueHandlingCode, initialState.step(), { _.next.step() })

  def run() = {
    val initialR = initialState.step()
    val iterator : Iterator[Plan3.State.Result[I1,I2,O]] = Enumerators.iterator[O,Plan3.State.Result[I1,I2,O]](factory.issueHandlingCode, initialR, { _.next.step() })
    val (_lastResult, _allOutput, _allIssues) = {
      iterator.foldLeft[(Plan3.State.Result[I1,I2,O], IndexedSeq[O],IndexedSeq[Issue])]((initialR, IndexedSeq[O](), IndexedSeq[Issue]())) {
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

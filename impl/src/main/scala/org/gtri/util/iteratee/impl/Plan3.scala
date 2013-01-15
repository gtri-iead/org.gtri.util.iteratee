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

import org.gtri.util.scala.exelog.noop._
import org.gtri.util.iteratee.api
import api._
import org.gtri.util.iteratee.impl.plan3._
import org.gtri.util.issue.api.Issue
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

// TODO: Comments and logs

object Plan3 {
  implicit val thisclass = classOf[Plan3[_,_,_]]
  implicit val log : Log = Logger.getLog(thisclass)
}
class Plan3[I1,I2,O](
  val factory: IterateeFactory,
  val enumerator : Enumerator[I1],
  val translator : Iteratee[I1,I2],
  val iteratee : Iteratee[I2,O]
) extends api.Plan3[I1,I2,O] {
  import Plan3._

  def initialState = State(enumerator.initialState, translator.initialState, iteratee.initialState)

  def iterator : java.util.Iterator[api.Plan3.State.Result[I1,I2,O]] =
    new EnumeratorIterator[O,api.Plan3.State.Result[I1,I2,O]](initialState.step(), { _.next.step() })

  def run() = {
    log.block("run") {
      ~"Step initial state"
      val initialR = initialState.step()
      ~"Create an iterator for this plan"
      val i : Iterator[api.Plan3.State.Result[I1,I2,O]] =
        new EnumeratorIterator[O,api.Plan3.State.Result[I1,I2,O]](initialR, { _.next.step() })
      ~"Fold on iterator, accumulating results and issues"
      val (_lastResult, _allOutput, _allIssues) = {
        i.foldLeft[(api.Plan3.State.Result[I1,I2,O], IndexedSeq[O],IndexedSeq[Issue])]((initialR, IndexedSeq[O](), IndexedSeq[Issue]())) {
          (accTuple, result) =>
            val (_, outputAcc, issuesAcc) = accTuple
            (result, result.output ++ outputAcc, result.issues ++ issuesAcc)
        }
      }
      ~"Feed endOfInput to last iteratee state"
      val eoiResult = _lastResult.next.endOfInput()
      ~"Return a proxy plan run result"
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
}

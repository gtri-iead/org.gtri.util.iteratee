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
import org.gtri.util.issue.api.Issue
import org.gtri.util.iteratee.api
import api._
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

object Plan2 {
  implicit val thisclass = classOf[Plan2[_,_]]
  implicit val log = Logger.getLog(thisclass)
}

class Plan2[I,O](val factory : IterateeFactory, val enumerator : Enumerator[I], val iteratee : Iteratee[I,O]) extends api.Plan2[I,O] {
  import Plan2._

  def initialState = new plan2.State(enumerator.initialState, iteratee.initialState)

  def iterator : java.util.Iterator[api.Plan2.State.Result[I,O]] = {
    new EnumeratorIterator[O,api.Plan2.State.Result[I,O]](
      r = initialState.step(),
      step = { _.next.step() }
    )
  }

  def run() = {
    log.block("run") {
      ~"Step the initial state once to get an initial result"
      val initialResult = initialState.step()
      ~"Get an iterator for the plan"
      val i : Iterator[api.Plan2.State.Result[I,O]] =
        new EnumeratorIterator[O,api.Plan2.State.Result[I,O]](initialResult, { _.next.step() })
      ~"Fold the enumerator, accumulating output and issues"
      val (_lastResult, _allOutput, _allIssues) = {
        i.foldLeft[(api.Plan2.State.Result[I,O], IndexedSeq[O],IndexedSeq[Issue])]((initialResult, IndexedSeq[O](), IndexedSeq[Issue]())) {
          (accTuple, result) =>
            val (_, outputAcc, issuesAcc) = accTuple
            (result, result.output ++ outputAcc, result.issues ++ issuesAcc)
        }
      }
      ~"Apply endOfInput to last result"
      val eoiResult = _lastResult.next.endOfInput()
      ~"Return a run result wrapper"
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
}

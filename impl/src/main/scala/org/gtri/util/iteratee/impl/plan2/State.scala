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
package org.gtri.util.iteratee.impl.plan2

import org.gtri.util.scala.exelog.noop._
import org.gtri.util.iteratee.api.{StatusCode, Iteratee, Enumerator}
import org.gtri.util.iteratee.api
import api.ImmutableBuffers
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

object State {
  implicit val thisclass = classOf[State[_,_]]
  implicit val log : Log = Logger.getLog(thisclass)
}
case class State[I,O](val enumeratorState : Enumerator.State[I], val iterateeState : Iteratee.State[I,O]) extends api.Plan2.State[I,O] {
  import State._

  def statusCode = {
    log.block("statusCode") {
      val eStatusCode = enumeratorState.statusCode
      val iStatusCode = iterateeState.statusCode
      ~s"and(enumeratorState.statusCode=$eStatusCode,iterateeState.statusCode=$iStatusCode)"
      StatusCode.and(enumeratorState.statusCode, iterateeState.statusCode)
    }
  }

  def progress = enumeratorState.progress

  def endOfInput() = {
    log.block("endOfInput") {
      +"Get eoi result from Iteratee"
      val iResult_EOI = iterateeState.endOfInput()
      +"Return a result with eoi output, overflow and issues"
      Result(
        next = State(enumeratorState, iResult_EOI.next),
        output = iResult_EOI.output,
        overflow = iResult_EOI.overflow,
        issues = iResult_EOI.issues
      )
    }
  }

  def step() = {
    log.block("step") {
      +"Are we done?"
      if (statusCode.isDone) {
        +"Plan is done"
        Result(this, ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
      } else {
        +"Plan is not done, step the enumerator"
        val eResult = enumeratorState.step()
        ~s"Apply enumerator to Iteratee"
        val iResult = iterateeState.apply(eResult.output)

        Result(
          next = State(eResult.next, iResult.next),
          output = iResult.output,
          overflow = iResult.overflow,
          issues = eResult.issues ++ iResult.issues
        )
      }
    }
  }
}

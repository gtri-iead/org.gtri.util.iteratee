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
package org.gtri.util.iteratee.impl.plan3

import org.gtri.util.scala.exelog.sideeffects._
import org.gtri.util.iteratee.api
import api.{StatusCode, ImmutableBuffers}
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

object State {
  implicit val classlog = ClassLog(classOf[State[_,_,_]])
}
case class State[I1,I2,O](
  val enumeratorState : api.Enumerator.State[I1],
  val translatorState : api.Iteratee.State[I1,I2],
  val iterateeState : api.Iteratee.State[I2,O]
) extends api.Plan3.State[I1,I2,O] {
  import State._

  def statusCode = {
    implicit val log = enter("statusCode")()
    val t1 = enumeratorState.statusCode
    val t2 = translatorState.statusCode
    val t3 = iterateeState.statusCode
    ~s"and(enumeratorState.statusCode=$t1,translatorState.statusCode=$t2,iterateeState.statusCode=$t3)"
    StatusCode.and(enumeratorState.statusCode, translatorState.statusCode, iterateeState.statusCode) <~: log
  }

  def progress = enumeratorState.progress

  def endOfInput() = {
    implicit val log = enter("endOfInput")()
    +"Apply EOI and build result"
    ~"Getting endOfInput result from translatorState"
    val tResult_EOI = translatorState.endOfInput()
    ~"Applying translatorState endOfInput result to iterateeState"
    val iResult = iterateeState.apply(tResult_EOI.output())
    ~"Getting endOfInput result from iterateeState"
    val iResult_EOI = iResult.next.endOfInput()
    Result(
      next = State(enumeratorState, tResult_EOI.next, iResult_EOI.next),
      output = iResult.output,
      overflow = iResult.overflow,
      issues = iResult.issues
    ) <~: log
  }

  def step() = {
    implicit val log = enter("step")()
    +"Are we done?"
    if(statusCode.isDone) {
      +"Yes, return an empty result"
      Result(this, ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty()) <~: log
    } else {
      +"Not done yet, step the enumerator"
      val eResult = enumeratorState.step()
      ~"Translate the results"
      val tResult = translatorState.apply(eResult.output)
      ~"Apply the translated results to the iteratee"
      val iResult = iterateeState.apply(tResult.output)
      ~"Return a result with Iteratee output/overflow and all logs appended"
      Result(
        next = State(eResult.next, tResult.next, iResult.next),
        output = iResult.output,
        overflow = iResult.overflow,
        issues = eResult.issues ++ tResult.issues ++ iResult.issues
      ) <~: log
    }
  }
}

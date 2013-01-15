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
import org.gtri.util.iteratee.api.{ImmutableBuffer, Iteratee, StatusCode}
import org.gtri.util.iteratee.impl.iteratees._
import ImmutableBufferConversions._

// TODO: Comments and logs

/**
* An Iteratee that is composed of two iteratees. The input of this Iteratee is fed to the first inner Iteratee
* and the output the first inner Iteratee is fed to input of the second Iteratee and the output of the second Iteratee
* becomes the output of this Iteratee.
* @param i1
* @param i2
* @tparam A
* @tparam B
* @tparam C
*/
case class IterateeSerialTuple2[A,B,C](
  i1 : Iteratee[A,B],
  i2 : Iteratee[B,C]
) extends Iteratee[A,C] {
  import IterateeSerialTuple2._

  def initialState = State(i1.initialState, i2.initialState, StatusCode.and(i1.initialState.statusCode, i2.initialState.statusCode))
}
object IterateeSerialTuple2 {

  object State {
    implicit val thisclass = classOf[State[_,_,_]]
    implicit val log : Log = Logger.getLog(thisclass)
  }
  case class State[A,B,C](
    i1 : Iteratee.State[A,B],
    i2 : Iteratee.State[B,C],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,C] {
    import State._
    def apply(input : A) = apply(Chunk(input))
    def apply(input: ImmutableBuffer[A]) = {
      log.block("apply",Seq("input#" -> input.length)) {
        +"Is this a done state?"
        if(statusCode.isDone) {
          +"Yes, return a result that returns input as overflow and stays here"
          Result(next = this, overflow = input)
        } else {
          +"No, apply input, accumulate results and return next result"
          ~"Apply input to i1"
          val resultI1 = i1.apply(input)
          ~"Apply result from i1 to i2"
          val resultI2 = i2.apply(resultI1.output)
          ~"Accumulate issues, overflow, output, statusCode and build next result"
          val issues = resultI1.issues ++ resultI2.issues
          val overflow = resultI1.overflow
          val output = resultI2.output
          val nextStatusCode = StatusCode.and(resultI1.next.statusCode, resultI2.next.statusCode)
          val next = State(resultI1.next, resultI2.next, nextStatusCode)
          Result(next, output, overflow, issues)
        }
      }
    }

    def endOfInput() = {
      log.block("endOfInput") {
        +"Is this a done state?"
        if(statusCode.isDone) {
          +"Yes, return a result that stays here"
          Result(next = this)
        } else {
          +"No, apply EOI, accumulate results and return next result"
          ~"Apply EOI to i1"
          val resultT1_EOI = i1.endOfInput()
          ~"Apply result of i1 to i2"
          val resultI2 = i2.apply(resultT1_EOI.output)
          ~"Apply result EOI to i2"
          val resultT2_EOI = i2.endOfInput()
          ~"Accumulate issues, overflow, output, statusCode and build next result"
          val issues = resultT1_EOI.issues ++ resultI2.issues ++ resultT2_EOI.issues
          val overflow = resultT1_EOI.overflow
          val output = resultI2.output ++ resultT2_EOI.output
          val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultT2_EOI.next.statusCode)
          val next = State(resultT1_EOI.next, resultT2_EOI.next, nextStatusCode)
          Result(next, output, overflow, issues)
        }
      }
    }
  }

}

case class IterateeSerialTuple3[A,B,C,D](
  i1 : Iteratee[A,B],
  i2 : Iteratee[B,C],
  i3 : Iteratee[C,D]
) extends Iteratee[A,D] {
  import IterateeSerialTuple3._

  def initialState = State(i1.initialState, i2.initialState, i3.initialState, StatusCode.and(i1.initialState.statusCode, i2.initialState.statusCode))
}
object IterateeSerialTuple3 {

  object State {
    implicit val thisclass = classOf[State[_,_,_,_]]
    implicit val log : Log = Logger.getLog(thisclass)
  }
  case class State[A,B,C,D](
    i1 : Iteratee.State[A,B],
    i2 : Iteratee.State[B,C],
    i3 : Iteratee.State[C,D],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,D] {
    import State._
    def apply(input : A) = apply(Chunk(input))
    def apply(input: ImmutableBuffer[A]) = {
      log.block("apply", Seq("input#" -> input.length)) {
        +"Is this a done state?"
        if(statusCode.isDone) {
          +"Yes, return a result that returns input as overflow and stays here"
          Result(next = this, overflow = input)
        } else {
          +"No, apply input, accumulate results and return next result"
          ~"Apply input to i1"
          val resultI1 = i1.apply(input)
          ~"Apply result of i1 to i2"
          val resultI2 = i2.apply(resultI1.output)
          ~"Apply result of i2 to i3"
          val resultI3 = i3.apply(resultI2.output)
          ~"Accumulate issues, overflow, output, statusCode and build next result"
          val issues = resultI1.issues ++ resultI2.issues ++ resultI3.issues
          val overflow = resultI1.overflow
          val output = resultI3.output
          val nextStatusCode = StatusCode.and(resultI1.next.statusCode, resultI2.next.statusCode, resultI3.next.statusCode)
          val next : Iteratee.State[A,D] = State(resultI1.next, resultI2.next, resultI3.next, nextStatusCode)
          Result(next, output, overflow, issues)
        }
      }
    }

    def endOfInput() = {
      log.block("endOfInput") {
        +"Is this a done state?"
        if(statusCode.isDone) {
          +"Yes, return a result that stays here"
          Result(next = this)
        } else {
          +"No, apply EOI, accumulate results and return next result"
          ~"Apply EOI to i1"
          val resultT1_EOI = i1.endOfInput()
          ~"Apply the result of i1 to i2"
          val resultI2_T1_EOI = i2.apply(resultT1_EOI.output)
          ~"Apply the result of i2 to i3"
          val resultI3_T1_EOI = i3.apply(resultI2_T1_EOI.output)
          ~"Apply EOI to i2"
          val resultT2_EOI = i2.endOfInput()
          ~"Apply result of i2 to i3"
          val resultI3_T2_EOI = i3.apply(resultT2_EOI.output)
          ~"Apply EOI to i3"
          val resultT3_EOI = i3.endOfInput()
          ~"Accumulate issues, overflow, output, statusCode and build next result"
          val issues = resultT1_EOI.issues ++ resultI2_T1_EOI.issues ++ resultI3_T1_EOI.issues ++ resultT2_EOI.issues ++ resultI3_T2_EOI.issues() ++ resultT3_EOI.issues
          val overflow = resultT1_EOI.overflow
          val output = resultI3_T1_EOI.output ++ resultI3_T2_EOI.output ++ resultT3_EOI.output
          val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultT2_EOI.next.statusCode, resultT3_EOI.next.statusCode)
          val next : Iteratee.State[A,D] = State(resultT1_EOI.next, resultT2_EOI.next, resultT3_EOI.next, nextStatusCode)
          Result(next, output, overflow, issues)
        }
      }
    }
  }

}
// TODO: IterateeSerialTuple4
// TODO: IterateeSerialTuple5
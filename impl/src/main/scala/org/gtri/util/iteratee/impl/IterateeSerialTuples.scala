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

import org.gtri.util.iteratee.api.{ImmutableBuffer, Iteratee, StatusCode}
import org.gtri.util.iteratee.impl.Iteratees._
import ImmutableBufferConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
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

  case class State[A,B,C](
    i1 : Iteratee.State[A,B],
    i2 : Iteratee.State[B,C],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,C] {
    def apply(input : A) = apply(Chunk(input))
    def apply(input: ImmutableBuffer[A]) = {
      if(statusCode.isDone) {
        Result[A,C](this)
      } else {
        val resultI1 = i1.apply(input)
        val resultI2 = i2.apply(resultI1.output)
        val issues = resultI1.issues ++ resultI2.issues
        val overflow = resultI1.overflow
        val output = resultI2.output
        val nextStatusCode = StatusCode.and(resultI1.next.statusCode, resultI2.next.statusCode)
        val next = State(resultI1.next, resultI2.next, nextStatusCode)
        Result(next, output, overflow, issues)
      }
    }

    def endOfInput() = {
      if(statusCode.isDone) {
        Result(this)
      } else {
        val resultT1_EOI = i1.endOfInput()
        val resultI2 = i2.apply(resultT1_EOI.output)
        val resultT2_EOI = i2.endOfInput()
        val issues = resultT1_EOI.issues ++ resultI2.issues ++ resultT2_EOI.issues
        val overflow = resultT1_EOI.overflow
        val output = resultI2.output ++ resultT2_EOI.output
        val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultI2.next.statusCode, resultT2_EOI.next.statusCode)
        val next = State(resultT1_EOI.next, resultT2_EOI.next, nextStatusCode)
        Result(next, output, overflow, issues)
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

  case class State[A,B,C,D](
    i1 : Iteratee.State[A,B],
    i2 : Iteratee.State[B,C],
    i3 : Iteratee.State[C,D],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,D] {

    def apply(input : A) = apply(Chunk(input))
    def apply(input: ImmutableBuffer[A]) = {
      if(statusCode.isDone) {
        Result(this)
      } else {
        val resultI1 = i1.apply(input)
        val resultI2 = i2.apply(resultI1.output)
        val resultI3 = i3.apply(resultI2.output)
        val issues = resultI1.issues ++ resultI2.issues ++ resultI3.issues
        val overflow = resultI1.overflow
        val output = resultI3.output
        val nextStatusCode = StatusCode.and(resultI1.next.statusCode, resultI2.next.statusCode, resultI3.next.statusCode)
        val next : Iteratee.State[A,D] = State(resultI1.next, resultI2.next, resultI3.next, nextStatusCode)
        Result(next, output, overflow, issues)
      }
    }

    def endOfInput() = {
      if(statusCode.isDone) {
        Result(this)
      } else {
        val resultT1_EOI = i1.endOfInput()
        val resultI2 = i2.apply(resultT1_EOI.output)
        val resultT2_EOI = i2.endOfInput()
        val resultI3 = i3.apply(resultT2_EOI.output)
        val resultT3_EOI = i3.endOfInput()
        val issues = resultT1_EOI.issues ++ resultI2.issues ++ resultT2_EOI.issues ++ resultI2.issues ++ resultT2_EOI.issues
        val overflow = resultT1_EOI.overflow
        val output = resultI3.output ++ resultT3_EOI.output
        val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultI2.next.statusCode, resultT2_EOI.next.statusCode, resultI3.next.statusCode, resultT3_EOI.next.statusCode)
        val next : Iteratee.State[A,D] = State(resultT1_EOI.next, resultT2_EOI.next, resultT3_EOI.next, nextStatusCode)
        Result(next, output, overflow, issues)
      }
    }

  }

}
// TODO: IterateeSerialTuple4
// TODO: IterateeSerialTuple5
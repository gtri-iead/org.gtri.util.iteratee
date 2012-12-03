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
import ImmutableBuffers.Conversions._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
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
    t1 : Iteratee.State[A,B],
    t2 : Iteratee.State[B,C],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,C] {

    def apply(input: ImmutableBuffer[A]) = {
      val resultT1 = t1.apply(input)
      val resultT2 = t2.apply(resultT1.output)
      val issues = resultT1.issues ++ resultT2.issues
      val overflow = resultT1.overflow
      val output = resultT2.output
      val nextStatusCode = StatusCode.and(resultT1.next.statusCode, resultT2.next.statusCode)
      val next : Iteratee.State[A,C] =
        nextStatusCode match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => State(resultT1.next, resultT2.next, nextStatusCode)
          case StatusCode.FATAL_ERROR => FatalError()
          case StatusCode.SUCCESS => Success()
        }
      Result(next, output, overflow, issues)
    }

    def endOfInput() = {
      val resultT1_EOI = t1.endOfInput()
      val resultT2 = t2.apply(resultT1_EOI.output)
      val resultT2_EOI = t2.endOfInput()
      val issues = resultT1_EOI.issues ++ resultT2.issues ++ resultT2_EOI.issues
      val overflow = resultT1_EOI.overflow
      val output = resultT2.output ++ resultT2_EOI.output
      val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultT2.next.statusCode, resultT2_EOI.next.statusCode)
      val next : Iteratee.State[A,C] =
        nextStatusCode match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => State(resultT1_EOI.next, resultT2_EOI.next, nextStatusCode)
          case StatusCode.FATAL_ERROR => FatalError()
          case StatusCode.SUCCESS => Success()
        }
      Result(next, output, overflow, issues)
    }

  }

}

case class IterateeSerialTuple3[A,B,C,D](
  t1 : Iteratee[A,B],
  t2 : Iteratee[B,C],
  t3 : Iteratee[C,D]
) extends Iteratee[A,D] {
  import IterateeSerialTuple3._

  def initialState = State(t1.initialState, t2.initialState, t3.initialState, StatusCode.and(t1.initialState.statusCode, t2.initialState.statusCode))
}
object IterateeSerialTuple3 {

  case class State[A,B,C,D](
    i1 : Iteratee.State[A,B],
    i2 : Iteratee.State[B,C],
    i3 : Iteratee.State[C,D],
    val statusCode : StatusCode
  ) extends Iteratee.State[A,D] {

    def apply(input: ImmutableBuffer[A]) = {
      val resultT1 = i1.apply(input)
      val resultT2 = i2.apply(resultT1.output)
      val resultT3 = i3.apply(resultT2.output)
      val issues = resultT1.issues ++ resultT2.issues ++ resultT3.issues
      val overflow = resultT1.overflow
      val output = resultT3.output
      val nextStatusCode = StatusCode.and(resultT1.next.statusCode, resultT2.next.statusCode, resultT3.next.statusCode)
      val next : Iteratee.State[A,D] =
        nextStatusCode match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => State(resultT1.next, resultT2.next, resultT3.next, nextStatusCode)
          case StatusCode.FATAL_ERROR => FatalError()
          case StatusCode.SUCCESS => Success()
        }
      Result(next, output, overflow, issues)
    }

    def endOfInput() = {
      val resultT1_EOI = i1.endOfInput()
      val resultT2 = i2.apply(resultT1_EOI.output)
      val resultT2_EOI = i2.endOfInput()
      val resultT3 = i3.apply(resultT2_EOI.output)
      val resultT3_EOI = i3.endOfInput()
      val issues = resultT1_EOI.issues ++ resultT2.issues ++ resultT2_EOI.issues ++ resultT2.issues ++ resultT2_EOI.issues
      val overflow = resultT1_EOI.overflow
      val output = resultT3.output ++ resultT3_EOI.output
      val nextStatusCode = StatusCode.and(resultT1_EOI.next.statusCode, resultT2.next.statusCode, resultT2_EOI.next.statusCode, resultT3.next.statusCode, resultT3_EOI.next.statusCode)
      val next : Iteratee.State[A,D] =
        nextStatusCode match {
          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => State(resultT1_EOI.next, resultT2_EOI.next, resultT3_EOI.next, nextStatusCode)
          case StatusCode.FATAL_ERROR => FatalError()
          case StatusCode.SUCCESS => Success()
        }
      Result(next, output, overflow, issues)
    }

  }

}

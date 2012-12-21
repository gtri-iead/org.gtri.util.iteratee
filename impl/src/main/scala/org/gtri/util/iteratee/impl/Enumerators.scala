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

import scala.collection.immutable.Seq
import org.gtri.util.iteratee.api
import org.gtri.util.iteratee.api._

import ImmutableBufferConversions._
import annotation.tailrec
import org.gtri.util.iteratee.api.Issue.ImpactCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 4:19 AM
 * To change this template use File | Settings | File Templates.
 */
object Enumerators {

  val STD_CHUNK_SIZE = 256

  def apply[A](seq : Seq[A]) = new SeqEnumerator(seq)

  case class Result[A](
    next : Enumerator.State[A],
    output : ImmutableBuffer[A],
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()
  ) extends Enumerator.State.Result[A]

  /**
   * A skeleton implementation for an Enumerator.State in the CONTINUE state.
   * @tparam A
   */
  abstract class Cont[A] extends Enumerator.State[A] {
    def statusCode = StatusCode.CONTINUE
  }

  /**
   * A skeleton implementation for an Enumerator.State that is done. The FSM will not change with further step calls.
   * @tparam A
   */
  abstract class BaseDone[A] extends Enumerator.State[A] {
    def step() = Result(this, ImmutableBuffers.empty())
  }

  /**
   * A convenience class for Success of an Enumerator. The FSM will not change with further step calls.
   * @param progress
   * @param output
   * @param issues
   * @tparam A
   */
  case class Success[A](
    progress : Progress,
    output : ImmutableBuffer[A] = ImmutableBuffers.empty[A](),
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()
  ) extends BaseDone[A] with api.Enumerator.State.Result[A] {
    def next = Success(progress)
    def statusCode = StatusCode.SUCCESS
  }

  /**
   * A convenience class for InputFailure of an Enumerator. The FSM will not change with further step calls.
   * @param progress
   * @param output
   * @param issues
   * @tparam A
   */
  case class Failure[A](
    progress : Progress,
    output : ImmutableBuffer[A] = ImmutableBuffers.empty[A](),
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()
  ) extends BaseDone[A] {
    def next = Failure(progress)
    def statusCode = StatusCode.FAILURE
  }

  /**
   * A class to get an Iterator of Enumerator.State.Result for an Enumerator
   * @param r
   * @param step
   * @tparam O
   * @tparam R
   */
  class EnumeratorIterator[O,R <: Enumerator.State.Result[O]](r : R, step: R => R) extends scala.Iterator[R] {
    var current = r

    def hasNext = current.next.statusCode.isDone == false

    def next() = {
      current = step(current)
      current
    }
  }

}


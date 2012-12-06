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
import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/26/12
* Time: 5:20 PM
* To change this template use File | Settings | File Templates.
*/
object Iteratees {
  type Chunk[A] = IndexedSeq[A]
  val Chunk = IndexedSeq

  object buffered {
    case class Result[I,O](next : Iteratee.State[I,O], output : ImmutableBuffer[O] = ImmutableBuffers.empty[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]) extends Iteratee.State.Result[I,O]

    abstract class BaseCont[I,O] extends Iteratee.State[I,O] {
      def statusCode = StatusCode.CONTINUE
    }
  }

  object unbuffered {
    case class Result[I,O](next : BaseState[I,O], output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) {
      def ++(rhs : Result[I,O]) = {
        new Result(rhs.next, rhs.output ++ output, rhs.overflow ++ overflow, rhs.issues ::: issues)
      }
    }

    implicit def resultToApiResult[I,O](result : Result[I,O]) : Iteratee.State.Result[I,O] = {
      new Iteratee.State.Result[I,O] {
        def next = result.next

        def output = result.output

        def overflow = result.overflow

        def issues = result.issues
      }
    }

    abstract class BaseState[I,O] extends Iteratee.State[I,O] {
      def apply(input : I) : Result[I,O]
    }

    abstract class BaseCont[I,O] extends BaseState[I,O] {
      def statusCode = StatusCode.CONTINUE

      def apply(inputs : ImmutableBuffer[I]) = {
        inputs.foldLeft(Result(this)) {
          (result, input) =>
            result ++ result.next(input)
        }
      }
    }
  }

  abstract class BaseDone[I,O] extends Iteratee.State[I,O] with Iteratee.State.Result[I,O] {
    def next = this
    // Return all input items as overflow
    def apply(items: ImmutableBuffer[I]) = buffered.Result(next = this, overflow = items)
//    def apply(item: I) = Result(next = this, overflow = Chunk(item))

    def endOfInput() = buffered.Result(this)
  }

  //  case class Done[I,O](val statusCode : StatusCode) extends BaseDone[I,O]
  class Success[I,O](val output : ImmutableBuffer[O] = ImmutableBuffers.empty[O], val overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I], val issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]) extends BaseDone[I,O] {
//    def this(_output : Chunk[O], _overflow : Chunk[I], _issues : List[Issue]) = this(_output, _overflow, _issues)
    def statusCode = StatusCode.SUCCESS
  }
  object Success {
    def apply[I,O]() = new Success[I,O](ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : O*) = new Success[I,O](Chunk(output:_*), ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O]) = new Success[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Success[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Success[I,O](output, overflow, issues)
    def apply[I,O](output : ImmutableBuffer[O]) = new Success[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Success[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Success[I,O](output, overflow, issues)
  }
  class Failure[I,O](val output : ImmutableBuffer[O] = ImmutableBuffers.empty[O], val overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I], val issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]) extends BaseDone[I,O] {
//    def this(_output : Chunk[O], _overflow : Chunk[I], _issues : List[Issue]) = this(_output, _overflow, _issues)
    def statusCode = StatusCode.FAILURE
  }
  object Failure {
    def apply[I,O]() = new Failure[I,O](ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : O*) = new Failure[I,O](Chunk(output:_*), ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O]) = new Failure[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Failure[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Failure[I,O](output, overflow, issues)
    def apply[I,O](output : ImmutableBuffer[O]) = new Failure[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Failure[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Failure[I,O](output, overflow, issues)
  }
  class Exception[I,O](val output : ImmutableBuffer[O] = ImmutableBuffers.empty[O], val overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I], val issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]) extends BaseDone[I,O] {
//    def this(output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = this(output, overflow, issues)
    def statusCode = StatusCode.EXCEPTION
  }
  object Exception {
    def apply[I,O]() = new Exception[I,O](ImmutableBuffers.empty, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : O*) = new Exception[I,O](Chunk(output:_*), ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O]) = new Exception[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Exception[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Exception[I,O](output, overflow, issues)
    def apply[I,O](output : ImmutableBuffer[O]) = new Exception[I,O](output, ImmutableBuffers.empty, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Exception[I,O](output, overflow, ImmutableBuffers.empty)
    def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Exception[I,O](output, overflow, issues)
  }
}

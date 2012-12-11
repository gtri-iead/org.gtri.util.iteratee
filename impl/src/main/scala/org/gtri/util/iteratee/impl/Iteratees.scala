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
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._
import annotation.tailrec

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

  case class Result[I,O](next : Iteratee.State[I,O], output : ImmutableBuffer[O] = ImmutableBuffers.empty[O](), overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I](), issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()) extends Iteratee.State.Result[I,O]

  abstract class MultiItemCont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE

    def apply(item : I)  = apply(Chunk(item))
  }

  abstract class SingleItemCont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE

    def apply(buffer : ImmutableBuffer[I]) = {
      doApply(buffer, 0, this, ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
    }

    @tailrec
    final def doApply(buffer : ImmutableBuffer[I], pos : Int, next : Iteratee.State[I,O], output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) : Result[I,O] = {
      if(pos >= buffer.length) {
        Result(next, output, overflow, issues)
      } else {
        val item = buffer(pos)
        val nextResult = next(item)
        val nextOutput = nextResult.output.append(output)
        val nextOverflow = nextResult.overflow.append(overflow)
        val nextIssues = nextResult.issues.append(issues)
        val nextPos = pos + 1
        nextResult.next.statusCode match {
          case StatusCode.EXCEPTION | StatusCode.FAILURE | StatusCode.SUCCESS=>
            val remaining = buffer.slice(pos, buffer.length)
            Result(nextResult.next, nextOutput, nextOverflow.append(remaining), nextIssues)
          case StatusCode.CONTINUE =>
            doApply(buffer,nextPos,nextResult.next, nextOutput, nextOverflow, nextIssues)
        }

      }
    }
  }

    abstract class BaseDone[I,O] extends Iteratee.State[I,O] {
      // Return all input items as overflow
      def apply(items: ImmutableBuffer[I]) = Result(next = this, overflow = items)
      def apply(item: I) = Result(next = this, overflow = Chunk(item))

      def endOfInput() = Result(this)
    }

    case class Success[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty[O](), overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I](), issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
      def next = this
      def statusCode = StatusCode.SUCCESS
    }
    object Success {
      def apply[I,O](output : O) = new Success[I,O](Chunk(output))
      def apply[I,O](output : O, overflow : ImmutableBuffer[I]) = new Success[I,O](Chunk(output), overflow)
      def apply[I,O](output : O, overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Success[I,O](Chunk(output), overflow, issues)
    }
    case class Failure[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty[O](), overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I](), issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
      def next = this
      def statusCode = StatusCode.FAILURE
    }
    case class Exception[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty(), overflow : ImmutableBuffer[I] = ImmutableBuffers.empty(), issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
      def next = this
      def statusCode = StatusCode.EXCEPTION
    }
}
//object Iteratees {
//  type Chunk[A] = IndexedSeq[A]
//  val Chunk = IndexedSeq
//
//  object buffered {
//    case class Result[I,O](next : Iteratee.State[I,O], output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends Iteratee.State.Result[I,O]
//
//    abstract class BaseCont[I,O] extends Iteratee.State[I,O] {
//      def statusCode = StatusCode.CONTINUE
//    }
//
//    abstract class BaseDone[I,O] extends Iteratee.State[I,O] {
//      // Return all input items as overflow
//      def apply(items: ImmutableBuffer[I]) = Result(next = this, overflow = items)
//
//      def endOfInput() = buffered.Result(this)
//    }
//
//    case class Success[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends BaseDone[I,O] with api.Iteratee.State.Result[I,O] {
//      def next = this
//      def statusCode = StatusCode.SUCCESS
//    }
//    case class Failure[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends BaseDone[I,O] with api.Iteratee.State.Result[I,O] {
//      def next = this
//      def statusCode = StatusCode.FAILURE
//    }
//    case class Exception[I,O](output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends BaseDone[I,O] with api.Iteratee.State.Result[I,O] {
//      def next = this
//      def statusCode = StatusCode.EXCEPTION
//    }
//  }
//
//  object unbuffered {
//    trait SingleResult[I,O] {
//      def next : BaseState[I,O]
//      def output : Chunk[O]
//      def overflow : Chunk[I]
//      def issues : List[Issue]
//    }
//    case class Result[I,O](next : BaseState[I,O], output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) extends SingleResult[I,O]
//    //    object Result {
//    //      def apply[I,O](next : BaseState[I,O], output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) = new Result(next, output, overflow, issues)
//    //    }
//
//    implicit def singleResultToApiResult[I,O](result : SingleResult[I,O]) : Iteratee.State.Result[I,O] = {
//      new Iteratee.State.Result[I,O] {
//        def next = result.next
//
//        def output = result.output
//
//        def overflow = result.overflow
//
//        def issues = result.issues
//      }
//    }
//
//    abstract class BaseState[I,O] extends Iteratee.State[I,O] {
//      def apply(input : I) : SingleResult[I,O]
//    }
//
//    abstract class BaseCont[I,O] extends BaseState[I,O] {
//      def statusCode = StatusCode.CONTINUE
//
//      def apply(inputs : ImmutableBuffer[I]) = {
//        // TODO: this should stop pushing input if Iteratee completes - rewrite as a recursive
//        inputs.foldLeft(Result(this)) {
//          (result, input) =>
//            val nextResult = result.next(input)
//            Result(nextResult.next, nextResult.output ++ result.output, nextResult.overflow ++ result.overflow, nextResult.issues ::: result.issues)
//        }
//      }
//
//    }
//
//    abstract class BaseDone[I,O] extends BaseState[I,O] {
//      // Return all input items as overflow
//      def apply(items: ImmutableBuffer[I]) = buffered.Result(next = this, overflow = items)
//      def apply(item: I) = Result(next = this, overflow = Chunk(item))
//
//      def endOfInput() = buffered.Result(this)
//    }
//
//    case class Success[I,O](output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) extends BaseDone[I,O] with SingleResult[I,O] {
//      def next = this
//      def statusCode = StatusCode.SUCCESS
//    }
//    case class Failure[I,O](output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) extends BaseDone[I,O] with SingleResult[I,O] {
//      def next = this
//      def statusCode = StatusCode.FAILURE
//    }
//    case class Exception[I,O](output : Chunk[O] = Chunk.empty, overflow : Chunk[I] = Chunk.empty, issues : List[Issue] = Nil) extends BaseDone[I,O] with SingleResult[I,O] {
//      def next = this
//      def statusCode = StatusCode.EXCEPTION
//    }
//    //    object Success {
//    //      def apply[I,O]() = new Success[I,O](ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : O*) = new Success[I,O](Chunk(output:_*), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O]) = new Success[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Success[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Success[I,O](output, overflow, issues)
//    //      def apply[I,O](output : ImmutableBuffer[O]) = new Success[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Success[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Success[I,O](output, overflow, issues)
//    //    }
//    //    class Failure[I,O](val output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], val overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], val issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends BaseDone[I,O] {
//    //      //    def this(_output : Chunk[O], _overflow : Chunk[I], _issues : List[Issue]) = this(_output, _overflow, _issues)
//    //      def statusCode = StatusCode.FAILURE
//    //    }
//    //    object Failure {
//    //      def apply[I,O]() = new Failure[I,O](ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : O*) = new Failure[I,O](Chunk(output:_*), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O]) = new Failure[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Failure[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Failure[I,O](output, overflow, issues)
//    //      def apply[I,O](output : ImmutableBuffer[O]) = new Failure[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Failure[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Failure[I,O](output, overflow, issues)
//    //    }
//    //    class Exception[I,O](val output : ImmutableBuffer[O] = ImmutableBuffers.empty()[O], val overflow : ImmutableBuffer[I] = ImmutableBuffers.empty()[I], val issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty()[Issue]) extends BaseDone[I,O] {
//    //      //    def this(output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = this(output, overflow, issues)
//    //      def statusCode = StatusCode.EXCEPTION
//    //    }
//    //    object Exception {
//    //      def apply[I,O]() = new Exception[I,O](ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : O*) = new Exception[I,O](Chunk(output:_*), ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O]) = new Exception[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I]) = new Exception[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : Chunk[O], overflow : Chunk[I], issues : List[Issue]) = new Exception[I,O](output, overflow, issues)
//    //      def apply[I,O](output : ImmutableBuffer[O]) = new Exception[I,O](output, ImmutableBuffers.empty(), ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I]) = new Exception[I,O](output, overflow, ImmutableBuffers.empty())
//    //      def apply[I,O](output : ImmutableBuffer[O], overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Exception[I,O](output, overflow, issues)
//    //    }
//  }
//
//}

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
import api.Issues._
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._
import annotation.tailrec
import org.gtri.util.iteratee.impl.box._

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

  case class Result[I,O](
    next : Iteratee.State[I,O],
    output : ImmutableBuffer[O] = ImmutableBuffers.empty[O](),
    overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I](),
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()
  ) extends Iteratee.State.Result[I,O]

//  implicit class boxToResult[I,O](box : Box[O]) {
//    def toResult(ifSuccess : O => Iteratee.State[I,O])(implicit issueHandlingCode : IssueHandlingCode) : Iteratee.State.Result[I,O] = {
//      // Return a result (recover once if needed)
//      box match {
//        // If box is Success then return a result with the output and the next state of ifSuccess
//        case SuccessBox(output, log) =>
//          Result(
//            next = ifSuccess(output),
//            output = Chunk(output),
//            issues = log
//          )
//        case RecoverBox(recoverable, log) =>
//          recoverable.recover.toOption match {
//            case Some(output) =>
//              Result(
//                next = ifSuccess(output),
//                output = Chunk(output),
//                issues = recoverable.recover.log ::: log
//              )
//            case None =>
//              Failure(issues = recoverable.recover.log ::: log)
//          }
//        // If Box is Failure then return an InputFailure
//        case FailBox(log) =>
//          Failure(issues = log)
//      }
//    }
//  }

  /**
   * Skeleton implementation class for Iteratees that process chunks of input. Derived classes implement only the
   * apply for chunks of input.
   *
   * @tparam I
   * @tparam O
   */
  abstract class MultiItemCont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE

    def apply(item : I)  = apply(Chunk(item))
  }

  /**
   * Skeleton implementation class for Iteratees that process one item at a time. Derived class implement only the
   * apply for single items.
   * @tparam I
   * @tparam O
   */
  abstract class SingleItemCont[I,O] extends Iteratee.State[I,O] {
    def statusCode = StatusCode.CONTINUE

    final def apply(buffer : ImmutableBuffer[I]) = {
      doApply(buffer, 0, this, ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
    }

    /**
     * Applies a buffer of input one item at a time, accumulating output, overflow and issues. If the iteratee is done
     * after processing a single item, returns the accumulated output and issues and appends the remaining input as
     * overflow
     *
     * @param buffer
     * @param pos
     * @param next
     * @param output
     * @param overflow
     * @param issues
     * @return
     */
    @tailrec final def doApply(
      buffer : ImmutableBuffer[I], pos : Int,
      next : Iteratee.State[I,O],
      output : ImmutableBuffer[O],
      overflow : ImmutableBuffer[I],
      issues : ImmutableBuffer[Issue]
    ) : Result[I,O] = {
      if(pos >= buffer.length) {
        Result(next, output, overflow, issues)
      } else {
        val item = buffer(pos)
        val nextResult = next(item)
        val nextOutput = nextResult.output.append(output)
        val nextOverflow = nextResult.overflow.append(overflow)
        val nextIssues = nextResult.issues.append(issues)
        val nextPos = pos + 1
        if(nextResult.next.statusCode.isDone) {
          // We are done - return anything remaining in the buffer as overflow
          val remaining = buffer.slice(pos, buffer.length)
          Result(nextResult.next, nextOutput, nextOverflow.append(remaining), nextIssues)
        } else {
          // Cont - recurse
          doApply(buffer,nextPos,nextResult.next, nextOutput, nextOverflow, nextIssues)
        }
      }
    }
  }

  abstract class BaseDone[I,O] extends Iteratee.State[I,O] {
    // Return all input log as overflow
    def apply(items: ImmutableBuffer[I]) = Result(next = this, overflow = items)
    def apply(item: I) = Result(next = this, overflow = Chunk(item))

    def endOfInput() = Result(this)
  }

  /**
   * Convenience class for a Success that is both a Iteratee.State and Iteratee.Result.  FSM will stay in this
   * state regardless of further input.
   * @param output
   * @param overflow
   * @param issues
   * @tparam I
   * @tparam O
   */
  case class Success[I,O](
    output : ImmutableBuffer[O] = ImmutableBuffers.empty[O](),
    overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I](),
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]()
  ) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
    def next = this
    def statusCode = StatusCode.SUCCESS
  }
  object Success {
    def apply[I,O](output : O) = new Success[I,O](Chunk(output))
    def apply[I,O](output : O, overflow : ImmutableBuffer[I]) = new Success[I,O](Chunk(output), overflow)
    def apply[I,O](output : O, overflow : ImmutableBuffer[I], issues : ImmutableBuffer[Issue]) = new Success[I,O](Chunk(output), overflow, issues)
  }

  /**
   * Convenience class for an input failure that is both a Iteratee.State and Iteratee.Result. FSM will stay in this
   * state regardless of further input.
   * @param output
   * @param overflow
   * @param issues
   * @tparam I
   * @tparam O
   */
  case class Failure[I,O](
    output : ImmutableBuffer[O] = ImmutableBuffers.empty[O],
    overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I],
    issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]
  ) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
    def next = this
    def statusCode = StatusCode.FAILURE
  }
}
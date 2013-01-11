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
package org.gtri.util.iteratee.impl.iteratees

import org.gtri.util.scala.exelog.sideeffects._
import org.gtri.util.issue.api.Issue
import org.gtri.util.iteratee.api._
import annotation.tailrec

object SingleItemCont {
  implicit val classlog = ClassLog(classOf[SingleItemCont[_,_]])
}
/**
* Skeleton implementation class for Iteratees that process one item at a time. Derived class implement only the
* apply for single items.
* @tparam I
* @tparam O
*/
abstract class SingleItemCont[I,O] extends Iteratee.State[I,O]  {
  import SingleItemCont._

  def statusCode = StatusCode.CONTINUE

  final def apply(buffer : ImmutableBuffer[I]) = {
    implicit val log = enter("apply") { ("buffer#" -> buffer.length) :: Nil }
    +"Applying each item in buffer individually"
    val result = doApply(buffer, 0, this, ImmutableBuffers.empty(), ImmutableBuffers.empty(), ImmutableBuffers.empty())
    +s"Final result=$result"
    result <~: log
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
    buffer : ImmutableBuffer[I],
    pos : Int,
    next : Iteratee.State[I,O],
    output : ImmutableBuffer[O],
    overflow : ImmutableBuffer[I],
    issues : ImmutableBuffer[Issue]
  ) : Result[I,O] = {
    implicit val log = enter("doApply") { "buffer#" -> buffer.length :: "pos" -> pos :: "next" -> next :: "output#" -> output.length :: "overflow#" -> overflow.length :: "issues#" -> issues :: Nil }
    ~"Still more input in buffer?"
    if(pos >= buffer.length) {
      ~"Finished iterating buffer"
      Result(next, output, overflow, issues)
    } else {
      val item = buffer(pos)
      val nextResult = next(item)
      val nextOutput = output.append(nextResult.output)
      val nextOverflow = overflow.append(nextResult.overflow)
      val nextIssues = issues.append(nextResult.issues)
      val nextPos = pos + 1
      ~s"Is next state done before input is finished?"
      if(nextResult.next.statusCode.isDone) {
        ~"We are done - return anything remaining in the buffer as overflow"
        val remaining = buffer.slice(pos, buffer.length)
        Result(nextResult.next, nextOutput, nextOverflow.append(remaining), nextIssues) <~: log
      } else {
        ~"Still accepting more input - recurse"
        doApply(buffer,nextPos,nextResult.next, nextOutput, nextOverflow, nextIssues)
      }
    }
  }
}

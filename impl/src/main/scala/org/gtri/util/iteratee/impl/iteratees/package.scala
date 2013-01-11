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

import box._
import org.gtri.util.scala.exelog.sideeffects._
import org.gtri.util.issue.api.IssueHandlingStrategy
import org.gtri.util.iteratee.api
import ImmutableBufferConversions._
import scala.collection.JavaConversions._

package object iteratees {
  type Chunk[+A] = IndexedSeq[A]
  val Chunk = IndexedSeq

  implicit class apiIterateeResult[I,O](self: api.Iteratee.State.Result[I,O]) {
    def toDebugString : String = {
      new StringBuilder(256)
        .append("Result(next.statusCode=")
        .append(self.next.statusCode)
        .append(",output#=")
        .append(self.output.length)
        .append(",overflow#=")
        .append(self.overflow.length)
        .append(",issues#=")
        .append(self.issues.length)
        .append(")").toString
    }
  }

  object boxToResult {
    implicit val classlog = ClassLog(classOf[boxToResult[_]])
  }
  implicit class boxToResult[O](box : Box[O]) {
    import boxToResult._
    def toResult[I, OO >: O](ifGo : O => api.Iteratee.State[I,OO])(implicit issueHandlingStrategy : IssueHandlingStrategy) : api.Iteratee.State.Result[I,OO] = {
      implicit val log = enter("toResult") { List("box" -> box, "issueHandlingStrategy" -> issueHandlingStrategy) }
      val boxlog = box.written
      box.value.fold(
        ifGo = { output =>
          ~"Box is Success - return a result with the output and the next state of ifGo"
          Result(
            next = ifGo(output),
            output = Chunk(output),
            issues = boxlog
          )
        },
        ifRecover = { recoverable =>
          log warn "Box is Recover - is issueHandlingCode set to RECOVER?"
          if(issueHandlingStrategy.canContinue(box.written.iterator)) {
            log warn "Yes, recover"
            val opt = recoverable.value
            val bothLogs = recoverable.written ::: boxlog
            log warn "Was recover successful?"
            if(opt.isDefined) {
              log warn "Yes, return result"
              val output = opt.get
              Result(
                next = ifGo(output),
                output = Chunk(output),
                issues = bothLogs
              )
            } else {
              log warn "No, recover failed return Failure"
              Failure(issues = bothLogs)
            }
          } else {
            log warn "No, issueHandlingCode not set to RECOVER return Failure"
            Failure(issues = boxlog)
          }
        },
        ifNoGo = {
          log warn "Box is nogo return Failure"
          Failure(issues = boxlog)
        }
      )
    }
  }

}

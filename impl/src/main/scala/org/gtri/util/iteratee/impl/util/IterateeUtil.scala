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
package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api.{StatusCode, IssueHandlingCode, Enumeratee}
import annotation.tailrec
import org.gtri.util.iteratee.api.Issue.ImpactCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
object IterateeUtil {
  def run[A,S,E <: Enumeratee[A,S]](issueHandlingCode : IssueHandlingCode, e : E, step : E => E) : E = {
    issueHandlingCode match {
      case IssueHandlingCode.NORMAL => doNormalRun[A,S,E](e, step)
      case IssueHandlingCode.LAX => doLaxRun[A,S,E](e, step)
      case IssueHandlingCode.STRICT => doStrictRun[A,S,E](e, step)
    }
  }

  @tailrec
  def doNormalRun[A,S,E <: Enumeratee[A,S]](e : E, step : E => E) : E = {
    if(e.status.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> e
        case StatusCode.CONTINUE => doNormalRun[A,S,E](step(e), step)
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }
  @tailrec
  def doLaxRun[A,S,E <: Enumeratee[A,S]](e : E, step : E => E) : E = {
    if(e.status.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> doLaxRun[A,S,E](step(e), step)
        case StatusCode.CONTINUE => doLaxRun[A,S,E](step(e), step)
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }
  @tailrec
  def doStrictRun[A,S,E <: Enumeratee[A,S]](e : E, step : E => E) : E = {
    if(e.status.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> e
        case StatusCode.CONTINUE =>
          if(e.issues().filter({ _.impactCode() == ImpactCode.WARNING}).nonEmpty) {
            e
          } else {
            doStrictRun[A,S,E](step(e), step)
          }
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }

}

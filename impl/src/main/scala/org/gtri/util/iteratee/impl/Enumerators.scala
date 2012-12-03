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
import org.gtri.util.iteratee.api._
import ImmutableBuffers.Conversions._
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

  case class Result[A](next : Enumerator.State[A], output : ImmutableBuffer[A], issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty) extends Enumerator.State.Result[A]

  abstract class Cont[A] extends Enumerator.State[A] {
    def statusCode = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A] extends Enumerator.State[A] {
    def statusCode = StatusCode.RECOVERABLE_ERROR
  }

  class Success[A](val progress : Progress) extends Enumerator.State[A] {
    def statusCode = StatusCode.SUCCESS

    def step() = Result(this, ImmutableBuffers.empty)
  }
  object Success {
    def apply[A](progress : Progress) = new Success[A](progress)

  }
  class FatalError[A](val progress : Progress) extends Enumerator.State[A] {
    def statusCode = StatusCode.FATAL_ERROR

    def step() = Result(this, ImmutableBuffers.empty)
  }
  object FatalError {
    def apply[A](progress : Progress) = new FatalError[A](progress)
  }

  def iterator[O,R <: Enumerator.State.Result[O]](issueHandlingCode : IssueHandlingCode, r : R, step: R => R) = {
    issueHandlingCode match {
      case IssueHandlingCode.NORMAL => new NormalRunIterator[O,R](r, step)
      case IssueHandlingCode.LAX => new LaxRunIterator[O,R](r, step)
      case IssueHandlingCode.STRICT => new StrictRunIterator[O,R](r, step)
    }

  }

  class NormalRunIterator[O,R <: Enumerator.State.Result[O]](var r : R, step: R => R) extends collection.Iterator[R] {
    def hasNext =
      r.next.statusCode match {
        case StatusCode.RECOVERABLE_ERROR=> false
        case StatusCode.CONTINUE => true
        case StatusCode.SUCCESS => false
        case StatusCode.FATAL_ERROR => false
      }
    def next() = {
      r = step(r)
      r
    }
  }

  class LaxRunIterator[O,R <: Enumerator.State.Result[O]](var r : R, step: R => R) extends collection.Iterator[R] {
    def hasNext =
      r.next.statusCode match {
        case StatusCode.RECOVERABLE_ERROR | StatusCode.CONTINUE => true
        case StatusCode.SUCCESS => false
        case StatusCode.FATAL_ERROR => false
      }
    def next() = {
      r = step(r)
      r
    }
  }

  class StrictRunIterator[O,R <: Enumerator.State.Result[O]](var r : R, step: R => R) extends collection.Iterator[R] {
    def hasNext =
      r.next.statusCode match {
        case StatusCode.RECOVERABLE_ERROR => false
        case StatusCode.CONTINUE => r.issues.filter({ _.impactCode == ImpactCode.WARNING }).isEmpty
        case StatusCode.SUCCESS => false
        case StatusCode.FATAL_ERROR => false
      }
    def next() = {
      r = step(r)
      r
    }
  }

//  def runFlatten[O,R <: Enumerator.State.Result[O]](issueHandlingCode : IssueHandlingCode, r: R, step: R => R) : (R, IndexedSeq[O], IndexedSeq[Issue]) = {
//    runFoldLeft[O,R,(R,IndexedSeq[O],IndexedSeq[Issue])](
//      issueHandlingCode,
//      r,
//      step,
//      (r, IndexedSeq[O](), IndexedSeq[Issue]()),
//      {
//        (accTuple, result) =>
//          val (resultAcc, allOutputsAcc, allIssuesAcc) = accTuple
//          (result, result.output ++ allOutputsAcc, result.issues ++ allIssuesAcc)
//      }
//    )
//  }
//
//  def runFoldLeft[O,R <: Enumerator.State.Result[O],U](issueHandlingCode : IssueHandlingCode, r: R, step: R => R, u: U, fold: (U,R) => U) : U = {
//    issueHandlingCode match {
//      case IssueHandlingCode.NORMAL => doNormalRun[O,R,U](r, step, u, fold)
//      case IssueHandlingCode.LAX => doLaxRun[O,R,U](r, step, u, fold)
//      case IssueHandlingCode.STRICT => doStrictRun[O,R,U](r, step, u, fold)
//    }
//  }
//  @tailrec
//  final def doNormalRun[O,R <: Enumerator.State.Result[O],U](r : R, step: R => R, u: U, fold: (U,R) => U) : U = {
//    val nextU = fold(u,r)
//    r.next.statusCode match {
//      case StatusCode.RECOVERABLE_ERROR=> nextU
//      case StatusCode.CONTINUE => {
//        doNormalRun[O,R,U](step(r), step, nextU, fold)
//      }
//      case StatusCode.SUCCESS => nextU
//      case StatusCode.FATAL_ERROR => nextU
//    }
//  }
//
//  @tailrec
//  final def doLaxRun[O,R <: Enumerator.State.Result[O],U](r : R, step: R => R, u: U, fold: (U,R) => U) : U = {
//    val nextU = fold(u,r)
//    r.next.statusCode match {
//      case StatusCode.RECOVERABLE_ERROR | StatusCode.CONTINUE => {
//        doLaxRun[O,R,U](step(r), step, nextU, fold)
//      }
//      case StatusCode.SUCCESS => nextU
//      case StatusCode.FATAL_ERROR => nextU
//    }
//  }
//
//  @tailrec
//  final def doStrictRun[O,R <: Enumerator.State.Result[O],U](r : R, step: R => R, u: U, fold: (U,R) => U) : U = {
//    val nextU = fold(u,r)
//    r.next.statusCode match {
//      case StatusCode.RECOVERABLE_ERROR=> nextU
//      case StatusCode.CONTINUE => {
//        if(r.issues.filter({ _.impactCode == ImpactCode.WARNING }).nonEmpty) {
//          nextU
//        } else {
//          doStrictRun[O,R,U](step(r), step, nextU, fold)
//        }
//      }
//      case StatusCode.SUCCESS => nextU
//      case StatusCode.FATAL_ERROR => nextU
//    }
//  }

}


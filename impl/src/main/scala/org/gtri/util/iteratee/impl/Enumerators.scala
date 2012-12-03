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

  def runFlatten[O,R <: Enumerator.State.Result[O]](issueHandlingCode : IssueHandlingCode, r: R, step: R => R) : (R, IndexedSeq[O], IndexedSeq[Issue]) = {
    val results = run[O,R](issueHandlingCode, r, step)

    val (allOutputs, allIssues) = results.foldLeft((IndexedSeq[O](), IndexedSeq[Issue]())) {
      (accTuple,result : R) => {
        val (accOutputs, accIssues) = accTuple
        (result.output ++ accOutputs, result.issues ++ accIssues)
      }
    }
    (results.head, allOutputs, allIssues)
  }

  // TODO: use stream here instead of List
  def run[O,R <: Enumerator.State.Result[O]](issueHandlingCode : IssueHandlingCode, r: R, step: R => R) : List[R] = {
    issueHandlingCode match {
      case IssueHandlingCode.NORMAL => doNormalRun[O,R](r, step, Nil)
      case IssueHandlingCode.LAX => doLaxRun[O,R](r, step, Nil)
      case IssueHandlingCode.STRICT => doStrictRun[O,R](r, step, Nil)
    }
  }
  @tailrec
  final def doNormalRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, results : List[R]) : List[R] = {
    val nextResults = r :: results
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR=> nextResults
      case StatusCode.CONTINUE => {
        doNormalRun[O,R](step(r), step, nextResults)
      }
      case StatusCode.SUCCESS => nextResults
      case StatusCode.FATAL_ERROR => nextResults
    }
  }

  @tailrec
  final def doLaxRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, results : List[R]) : List[R] = {
    val nextResults = r :: results
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR | StatusCode.CONTINUE => {
        doLaxRun[O,R](step(r), step, nextResults)
      }
      case StatusCode.SUCCESS => nextResults
      case StatusCode.FATAL_ERROR => nextResults
    }
  }

  @tailrec
  final def doStrictRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, results : List[R]) : List[R] = {
    val nextResults = r :: results
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR=> nextResults
      case StatusCode.CONTINUE => {
        if(r.issues.filter({ _.impactCode == ImpactCode.WARNING }).nonEmpty) {
          nextResults
        } else {
          doStrictRun[O,R](step(r), step, nextResults)
        }
      }
      case StatusCode.SUCCESS => nextResults
      case StatusCode.FATAL_ERROR => nextResults
    }
  }

}


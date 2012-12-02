package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api._
import collection.immutable.Traversable
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

  def apply[A](traversable : Traversable[A]) = new TraversableEnumerator(traversable)

  case class Result[A](next : Enumerator.State[A], output : Traversable[A], issues : Traversable[Issue] = Nil) extends Enumerator.State.Result[A]

  abstract class Cont[A] extends Enumerator.State[A] {
    def statusCode = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A] extends Enumerator.State[A] {
    def statusCode = StatusCode.RECOVERABLE_ERROR
  }

  class Success[A](val progress : Progress) extends Enumerator.State[A] {
    def statusCode = StatusCode.SUCCESS

    def step() = Result(this, Nil)
  }
  object Success {
    def apply[A](progress : Progress) = new Success[A](progress)

  }
  class FatalError[A](val progress : Progress) extends Enumerator.State[A] {
    def statusCode = StatusCode.FATAL_ERROR

    def step() = Result(this, Nil)
  }
  object FatalError {
    def apply[A](progress : Progress) = new FatalError[A](progress)
  }

  def run[O,R <: Enumerator.State.Result[O]](issueHandlingCode : IssueHandlingCode, r: R, step: R => R) : (R, List[Traversable[O]], Traversable[Issue]) = {
    issueHandlingCode match {
      case IssueHandlingCode.NORMAL => doNormalRun[O,R](r, step, Nil, Nil)
      case IssueHandlingCode.LAX => doLaxRun[O,R](r, step, Nil, Nil)
      case IssueHandlingCode.STRICT => doStrictRun[O,R](r, step, Nil, Nil)
    }
  }
  @tailrec
  final def doNormalRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, output : List[Traversable[O]], issues : Traversable[Issue]) : (R, List[Traversable[O]], Traversable[Issue]) = {
    val nextIssues = r.issues ++ issues
    val nextOutput = r.output :: output
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR=> (r, nextOutput, nextIssues)
      case StatusCode.CONTINUE => {
        doNormalRun(step(r), step, nextOutput, nextIssues)
      }
      case StatusCode.SUCCESS => (r, nextOutput, nextIssues)
      case StatusCode.FATAL_ERROR => (r, nextOutput, nextIssues)
    }
  }

  @tailrec
  final def doLaxRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, output : List[Traversable[O]], issues : Traversable[Issue]) : (R, List[Traversable[O]], Traversable[Issue]) = {
    val nextIssues = r.issues ++ issues
    val nextOutput = r.output :: output
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR | StatusCode.CONTINUE => {
        doLaxRun(step(r), step, nextOutput, nextIssues)
      }
      case StatusCode.SUCCESS => (r, nextOutput, nextIssues)
      case StatusCode.FATAL_ERROR => (r, nextOutput, nextIssues)
    }
  }

  @tailrec
  final def doStrictRun[O,R <: Enumerator.State.Result[O]](r : R, step: R => R, output : List[Traversable[O]], issues : Traversable[Issue]) : (R, List[Traversable[O]], Traversable[Issue]) = {
    val nextIssues = r.issues ++ issues
    val nextOutput = r.output :: output
    r.next.statusCode match {
      case StatusCode.RECOVERABLE_ERROR=> (r, nextOutput, nextIssues)
      case StatusCode.CONTINUE => {
        if(r.issues.filter({ _.impactCode == ImpactCode.WARNING }).nonEmpty) {
          (r, nextOutput, nextIssues)
        } else {
          doStrictRun(step(r), step, nextOutput, nextIssues)
        }
      }
      case StatusCode.SUCCESS => (r, nextOutput, nextIssues)
      case StatusCode.FATAL_ERROR => (r, nextOutput, nextIssues)
    }
  }

}

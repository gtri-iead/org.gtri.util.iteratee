package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api._
import annotation.tailrec
import org.gtri.util.iteratee.api.Issue.ImpactCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */
class Looper[A,B,S](val enumeratee : Enumeratee[A], val translatee : Translatee[A,B], val iteratee : Iteratee[B,S], val issues : Traversable[Issue] = Traversable.empty, val overflow : Traversable[B] = Traversable.empty) {
  def loopOnce() : Looper[A,B,S] = {
    val eResult = enumeratee.step()
    val tResult = translatee(eResult.output)
    val iResult = iteratee(tResult.output)
    new Looper[A,B,S](eResult.next, tResult.next, iResult.next, iResult.issues ++ tResult.issues ++ issues, iResult.overflow ++ overflow)
  }

  def run(issueHandlingCode : IssueHandlingCode) : Looper[A,B,S] = {
    issueHandlingCode match {
      case IssueHandlingCode.NORMAL => doNormalRun()
      case IssueHandlingCode.LAX => doLaxRun()
      case IssueHandlingCode.STRICT => doStrictRun()
    }
  }

  @tailrec
  final def doNormalRun() : Looper[A,B,S] = {
    val status = StatusCode.And(enumeratee.status, translatee.status)
    if(status.isDone ) {
      this
    } else {
      status match {
        case StatusCode.RECOVERABLE_ERROR=> this
        case StatusCode.CONTINUE => loopOnce().doNormalRun()
        case StatusCode.SUCCESS => this
        case StatusCode.FATAL_ERROR => this
      }
    }
  }

  @tailrec
  final def doLaxRun() : Looper[A,B,S] = {
    val status = StatusCode.And(enumeratee.status, translatee.status)
    if(status.isDone ) {
      this
    } else {
      status match {
        case StatusCode.RECOVERABLE_ERROR => loopOnce().doLaxRun()
        case StatusCode.CONTINUE => loopOnce().doLaxRun()
        case StatusCode.SUCCESS => this
        case StatusCode.FATAL_ERROR => this
      }
    }
  }
  @tailrec
  final def doStrictRun() : Looper[A,B,S] = {
    val status = StatusCode.And(enumeratee.status, translatee.status)
    if(status.isDone ) {
      this
    } else {
      status match {
        case StatusCode.RECOVERABLE_ERROR=> this
        case StatusCode.CONTINUE =>
          if(issues.filter({ _.impactCode() == ImpactCode.WARNING}).nonEmpty) {
            this
          } else {
            loopOnce().doStrictRun()
          }
        case StatusCode.SUCCESS => this
        case StatusCode.FATAL_ERROR => this
      }
    }
  }

}

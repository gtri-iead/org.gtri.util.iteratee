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

import org.gtri.util.iteratee.api.{IssueHandlingCode, ImmutableDiagnosticLocator, Issue}
import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator._
import scala._
import scala.Some
import org.gtri.util.iteratee.api.Issue.ImpactCode
import org.gtri.util.iteratee.impl.Issues.FatalError

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
class OptIssueWriter[+A](_item : Option[A],val issues : List[Issue] = Nil)(implicit issueHandlingCode : IssueHandlingCode) {
  // Discard item if issues list contains a "failure" as defined by the issue handling code
  val item : Option[A] = {
    val isFail = {
      issueHandlingCode match {
        case IssueHandlingCode.NORMAL => {
          (issue : Issue) =>
            issue.impactCode match {
              case ImpactCode.FATAL_ERROR => true
              case ImpactCode.RECOVERABLE_ERROR => true
              case _ => false
            }
        }
        case IssueHandlingCode.LAX => {
          (issue : Issue) =>
            issue.impactCode match {
              case ImpactCode.FATAL_ERROR => true
              case ImpactCode.RECOVERABLE_ERROR => false
              case _ => false
            }
        }
        case IssueHandlingCode.STRICT =>{
          (issue : Issue) =>
            issue.impactCode match {
              case ImpactCode.FATAL_ERROR => true
              case ImpactCode.RECOVERABLE_ERROR => true
              case ImpactCode.WARNING => true
              case _ => false
            }
        }
      }
    }

    if(_item.isEmpty || issues.find(isFail).isDefined) {
      None
    } else {
      _item
    }
  }
  def <<(anIssue : Issue) = new OptIssueWriter(item, List(anIssue))
  def <<(moreIssues : List[Issue]) = new OptIssueWriter(item, moreIssues)
//  def ++(writer : OptIssueWriter[A]) = new OptIssueWriter(writer.item orElse item, writer.issues ::: issues)

  def flatMap[B](f: A => OptIssueWriter[B]) : OptIssueWriter[B] = {
    if(item.isDefined) {
      try {
        val o = f(item.get)
        new OptIssueWriter(o.item, o.issues ::: issues)
      } catch {
        case e:Exception =>
          OptIssueWriter(None, FatalError(e) :: issues)
      }
    } else {
      new OptIssueWriter(None, issues)
    }
  }

  def map[B](f: A => B) : OptIssueWriter[B] = {
    if(item.isDefined) {
      try {
        new OptIssueWriter(Some(f(item.get)), issues)
      } catch {
        case e: Exception =>
          new OptIssueWriter(None, FatalError(e) :: issues)
      }
    } else {
      new OptIssueWriter[B](None)
    }
  }

  def withFilter(p: A => Boolean) : OptIssueWriter[A] = {
    if(item.isDefined) {
      if(p(item.get)) {
        this
      } else {
        new OptIssueWriter(None, issues)
      }
    } else {
      this
    }
  }

  def foreach(f: A => Unit) {
    if(item.isDefined) {
      f(item.get)
    }
  }

}
object OptIssueWriter {
  def tell(anIssue : Issue, moreIssues : Issue*)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter[Unit](Some(()), List(moreIssues:_*) ::: List(anIssue))
  def tell(moreIssues : Traversable[Issue])(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter[Unit](Some(()), moreIssues.toList)

  def fatalError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(Issues.FatalError(e, locator)))
  def recoverableError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(Issues.RecoverableError(e, locator)))
  def warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array())(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(Issues.Warning(message, locator, stackTrace)))
  def debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array())(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(Issues.Debug(message, locator, stackTrace)))
  def info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array())(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(Issues.Info(message, locator, stackTrace)))

  def issue(anIssue : Issue, moreIssues : Issue*)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, List(moreIssues:_*) ::: List(anIssue))
  def issue(moreIssues : Traversable[Issue])(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(None, moreIssues.toList)

  def apply[A](item : A)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(Some(item))

  def tryApply[A](lazyA: => A)(implicit issueHandlingCode : IssueHandlingCode) : OptIssueWriter[A] = {
    try {
      // lazyA will only throw when evaluated
      new OptIssueWriter(Some(lazyA))
    } catch {
      case e:Exception => fatalError(e)
    }
  }

  def apply[A](writer : OptIssueWriter[A], moreIssues : List[Issue])(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(writer.item, moreIssues ::: writer.issues)
  def apply[A](item : Option[A], issues : List[Issue], moreIssues : List[Issue])(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(item, moreIssues ::: issues)
  def apply[A](item : Option[A], issues : List[Issue])(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(item, issues)
  def apply[A](item : Option[A], issue : Issue)(implicit issueHandlingCode : IssueHandlingCode) = new OptIssueWriter(item, List(issue))
}


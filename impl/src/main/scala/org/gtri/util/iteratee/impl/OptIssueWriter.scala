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

import org.gtri.util.iteratee.api.{ImmutableDiagnosticLocator, Issue}
import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator._
import scala._
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
class OptIssueWriter[+A](val item : Option[A],val issues : List[Issue] = Nil) {
  def <<(anIssue : Issue) = new OptIssueWriter(item, anIssue :: issues)
  def <<(moreIssues : List[Issue]) = new OptIssueWriter(item, moreIssues ::: issues)
  def <<(moreIssues : Seq[Issue]) = new OptIssueWriter(item, List(moreIssues:_*) ::: issues)


  def flatMap[B](f: A => OptIssueWriter[B]) : OptIssueWriter[B] = {
    if(item.isDefined) {
      try {
        val o = f(item.get)
        new OptIssueWriter(o.item, o.issues ::: issues)
      } catch {
        case e:Exception => OptIssueWriter.fatalError(e)
      }
    } else {
      new OptIssueWriter(None, issues)
    }
  }

  def map[B](f: A => B) : OptIssueWriter[B] = {
    if(item.isDefined) {
      try {
        OptIssueWriter(f(item.get))
      } catch {
        case e: Exception => OptIssueWriter.fatalError(e)
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
  def tell(anIssue : Issue, moreIssues : Issue*) = new OptIssueWriter[Unit](Some(()), List(moreIssues:_*) ::: List(anIssue))
  def tell(moreIssues : Traversable[Issue]) = new OptIssueWriter[Unit](Some(()), moreIssues.toList)
  def fatalError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new OptIssueWriter(None, List(Issues.FatalError(e, locator)))
  def recoverableError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new OptIssueWriter(None, List(Issues.RecoverableError(e, locator)))
  def warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptIssueWriter(None, List(Issues.Warning(message, locator, stackTrace)))
  def debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptIssueWriter(None, List(Issues.Debug(message, locator, stackTrace)))
  def info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptIssueWriter(None, List(Issues.Info(message, locator, stackTrace)))
  def issue(anIssue : Issue, moreIssues : Issue*) = new OptIssueWriter(None, List(moreIssues:_*) ::: List(anIssue))
  def issue(moreIssues : Traversable[Issue]) = new OptIssueWriter(None, moreIssues.toList)
  def apply[A](item : A) = new OptIssueWriter(Some(item))
  def tryApply[A](lazyA: => A) : OptIssueWriter[A] = {
    try {
      // lazyA will only throw when evaluated
      new OptIssueWriter(Some(lazyA))
    } catch {
      case e:Exception => fatalError(e)
    }
  }
}

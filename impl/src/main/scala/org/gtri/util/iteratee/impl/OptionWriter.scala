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
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
class OptionWriter[+A](val item : Option[A],val issues : List[Issue] = Nil) {
  def <<(anIssue : Issue) = new OptionWriter(item, anIssue :: issues)
  def <<(moreIssues : List[Issue]) = new OptionWriter(item, moreIssues ::: issues)
  def <<(moreIssues : Seq[Issue]) = new OptionWriter(item, List(moreIssues:_*) ::: issues)


  def flatMap[B](f: A => OptionWriter[B]) : OptionWriter[B] = {
    if(item.isDefined) {
      try {
        val o = f(item.get)
        new OptionWriter(o.item, issues ::: o.issues)
      } catch {
        case e:Exception => OptionWriter.fatalError(e)
      }
    } else {
      new OptionWriter(None, issues)
    }
  }

  def map[B](f: A => B) : OptionWriter[B] = {
    if(item.isDefined) {
      try {
        OptionWriter(f(item.get))
      } catch {
        case e: Exception => OptionWriter.fatalError(e)
      }
    } else {
      new OptionWriter[B](None)
    }
  }
}
object OptionWriter {
  def tell(anIssue : Issue) = new OptionWriter[Unit](Some(()), List(anIssue))
  def tell(moreIssues : List[Issue]) = new OptionWriter[Unit](Some(()), moreIssues)
  def fatalError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new OptionWriter(None, List(Issues.FatalError(e, locator)))
  def recoverableError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new OptionWriter(None, List(Issues.RecoverableError(e, locator)))
  def warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptionWriter(None, List(Issues.Warning(message, locator, stackTrace)))
  def debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptionWriter(None, List(Issues.Debug(message, locator, stackTrace)))
  def info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new OptionWriter(None, List(Issues.Info(message, locator, stackTrace)))
  def issue(anIssue : Issue) = new OptionWriter(None, List(anIssue))
  def issue(moreIssues : List[Issue]) = new OptionWriter(None, moreIssues)
  def apply[A](item : A) = new OptionWriter(Some(item))
  def tryCreate[A](f: () => A) : OptionWriter[A] = {
    try {
      new OptionWriter(Some(f()))
    } catch {
      case e:Exception => fatalError(e)
    }
  }
}

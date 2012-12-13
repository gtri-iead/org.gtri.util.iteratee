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

import org.gtri.util.iteratee.api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
class OptIssueWriter[+A](val valueOrFail : Option[A], val issues : List[Issue] = Nil) {

  def isValue = valueOrFail.isDefined
  def isFail = valueOrFail.isEmpty

  def ++(anIssue : Issue) = new OptIssueWriter(valueOrFail, anIssue :: issues)
  def ++(moreIssues : List[Issue]) = new OptIssueWriter(valueOrFail, moreIssues ::: issues)

  def flatMap[B](f: Option[A] => OptIssueWriter[B]) : OptIssueWriter[B] = {
    val innerWriter = f(valueOrFail)
    new OptIssueWriter[B](innerWriter.valueOrFail, innerWriter.issues ::: issues)
  }

  def map[B](f: Option[A] => Option[B]) : OptIssueWriter[B] = {
    new OptIssueWriter[B](f(valueOrFail), issues)
  }

  //  def flatMap[B](f: A => OptIssueWriter[B]) : OptIssueWriter[B] = {
//    valueOrFail match {
//      case Some(value) =>
//        val innerWriter = f(value)
//        new OptIssueWriter[B](innerWriter.valueOrFail, innerWriter.issues ::: issues)
//      case None =>
//        new OptIssueWriter[B](None, issues)
//    }
//  }
//
//  def map[B](f: A => B) : OptIssueWriter[B] = {
//    valueOrFail match {
//      case Some(value) =>
//        new OptIssueWriter[B](Some(f(value)), issues)
//      case None =>
//        new OptIssueWriter[B](None, issues)
//    }
//  }
//
//  def withFilter(p: A => Boolean) : OptIssueWriter[A] = {
//    valueOrFail match {
//      case Some(value) =>
//        if(p(value)) {
//          this
//        } else {
//          new OptIssueWriter(None, issues)
//        }
//      case None => this
//    }
//  }
//
//  def foreach(f: A => Unit) {
//    valueOrFail match {
//      case Some(value) => f(value)
//      case None => // noop
//    }
//  }

}
object OptIssueWriter {
  def tell(anIssue : Issue, moreIssues : Issue*) =
    new OptIssueWriter[Unit](Some(()), List(moreIssues:_*) ::: List(anIssue))
  def tell(moreIssues : Traversable[Issue]) =
    new OptIssueWriter[Unit](Some(()), moreIssues.toList)

//  def doRecover[A](default: => A, cause : Issue, handler: Exception => Issue)(implicit issueHandlingStrategy : IssueHandlingStrategy) : OptIssueWriter[A] = {
//    // Can we recover from this issue?
//    if(issueHandlingStrategy.canContinue(cause)) {
//      // Yes, recover
//      val recoverWriter = tryFail(default,handler)
//      new OptIssueWriter(recoverWriter.valueOrFail, recoverWriter.issues ::: List(cause))
//    } else {
//      // No, return a fail
//      new OptIssueWriter(None, List(cause))
//    }
//  }
//
//  def tryRecover[A](lazyA: => A, handler: Exception => Issue, recover: => A)(implicit issueHandlingStrategy : IssueHandlingStrategy) : OptIssueWriter[A] = {
//    try {
//      // lazyA will only throw when evaluated
//      new OptIssueWriter(Some(lazyA))
//    } catch {
//      // lazyA threw an exception
//      case e:Exception =>
//        // Convert the exception to an issue
//        val issue = handler(e)
//        doRecover(recover, issue, handler)
//    }
//  }
//
//  def tryFail[A](lazyA: => A, handler: Exception => Issue) : OptIssueWriter[A] = {
//    try {
//      // lazyA will only throw when evaluated
//      new OptIssueWriter(Some(lazyA))
//    } catch {
//      // lazyA threw an exception
//      case e:Exception =>
//        // Convert the exception to an issue
//        val issue = handler(e)
//        // Return a fail
//        new OptIssueWriter(None, List(issue))
//    }
//  }

  def apply[A](item : A) =
    new OptIssueWriter(Some(item), Nil)
  def apply[A](item : A, issues : List[Issue]) =
    new OptIssueWriter(Some(item), issues)
  def apply[A](issues : List[Issue]) =
    new OptIssueWriter[A](None, issues)
  def apply[A](issues : Issue) =
    new OptIssueWriter[A](None, List(issues))
  def apply[A]() =
    new OptIssueWriter[A](None, Nil)
}


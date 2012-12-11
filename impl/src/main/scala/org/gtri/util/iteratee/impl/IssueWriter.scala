//package org.gtri.util.iteratee.impl
//
//import org.gtri.util.iteratee.api.{ImmutableDiagnosticLocator, Issue}
//import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator._
//import scala.Some
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 12/5/12
// * Time: 1:56 AM
// * To change this template use File | Settings | File Templates.
// */
//class IssueWriter[+A](val item : A,val issues : List[Issue] = Nil) {
//  def <<(anIssue : Issue) = new IssueWriter(item, anIssue :: issues)
//  def <<(moreIssues : List[Issue]) = new IssueWriter(item, moreIssues ::: issues)
//  def <<(moreIssues : Seq[Issue]) = new IssueWriter(item, List(moreIssues:_*) ::: issues)
//
//
//  def flatMap[B](f: A => IssueWriter[B]) : IssueWriter[B] = {
//    val o = f(item)
//    new IssueWriter(o.item, o.issues ::: issues)
//  }
//
//  def map[B](f: A => B) : IssueWriter[B] = {
//    IssueWriter(f(item))
//  }
//
//  def foreach(f: A => Unit) {
//    f(item)
//  }
//
//}
//object IssueWriter {
//  def tell(anIssue : Issue, moreIssues : Issue*) = new IssueWriter[Unit](Some(()), List(moreIssues:_*) ::: List(anIssue))
//  def tell(moreIssues : Traversable[Issue]) = new IssueWriter[Unit](Some(()), moreIssues.toList)
//  def fatalError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new IssueWriter(None, List(Issues.FatalError(e, locator)))
//  def recoverableError(e : Exception, locator : ImmutableDiagnosticLocator = ImmutableDiagnosticLocator.nowhere) = new IssueWriter(None, List(Issues.RecoverableError(e, locator)))
//  def warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new IssueWriter(None, List(Issues.Warning(message, locator, stackTrace)))
//  def debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new IssueWriter(None, List(Issues.Debug(message, locator, stackTrace)))
//  def info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) = new IssueWriter(None, List(Issues.Info(message, locator, stackTrace)))
//  def issue(anIssue : Issue, moreIssues : Issue*) = new IssueWriter(None, List(moreIssues:_*) ::: List(anIssue))
//  def issue(moreIssues : Traversable[Issue]) = new IssueWriter(None, moreIssues.toList)
//  def apply[A](item : A) = new IssueWriter(item)
//}

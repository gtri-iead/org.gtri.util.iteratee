package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/19/12
* Time: 7:21 PM
* To change this template use File | Settings | File Templates.
*/
abstract class BaseTranslatee[A,B,S](val downstream : Iteratee[B,S], localIssues : List[Issue] = Nil) extends Translatee[A,B,S] {
  def status = downstream.status

  def issues = localIssues ::: downstream.issues

  def overflow = Nil

  def isDone = downstream.isDone

  def state = downstream.state
}

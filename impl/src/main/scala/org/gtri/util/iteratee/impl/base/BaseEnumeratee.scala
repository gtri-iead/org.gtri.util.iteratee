package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api._
import annotation.tailrec

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseEnumeratee[A,S](val downstream : Iteratee[A,S], val issues : List[Issue] = Nil) extends Enumeratee[A,S] {

  def status() = {
    if(isDone) {
      StatusCode.CONTINUE
    } else {
      StatusCode.SUCCESS
    }
  }

  // TODO : how to inject issues here?
  def state = downstream.state

  def run() = doRun(this)

  @tailrec
  private def doRun[A,S](e : Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.isDone) {
      e
    } else {
      doRun(e.step())
    }
  }
}

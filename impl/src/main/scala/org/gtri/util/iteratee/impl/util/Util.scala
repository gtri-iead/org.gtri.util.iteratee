package org.gtri.util.iteratee.impl.util

import annotation.tailrec
import org.gtri.util.iteratee.api.Enumeratee

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/19/12
* Time: 2:18 AM
* To change this template use File | Settings | File Templates.
*/
object Util {
  @tailrec
  def run[A,S](e: Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.status.isDone) {
      e
    } else {
      run(e.step())
    }
  }
}

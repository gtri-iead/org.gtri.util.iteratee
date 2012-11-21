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
abstract class BaseEnumeratee[A,S](val downstream : Iteratee[A,S], localIssues : List[Issue] = Nil) extends Enumeratee[A,S] {
  def issues = localIssues ::: downstream.issues

  def status = downstream.status

  def state = downstream.state

  def run = doRun(this)

  @tailrec
  private def doRun[A,S](e : Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.isDone) {
      e
    } else {
      doRun(e.step)
    }
  }
}

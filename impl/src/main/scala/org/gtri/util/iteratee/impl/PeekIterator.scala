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

import collection.mutable.Queue

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/15/12
* Time: 11:46 AM
* To change this template use File | Settings | File Templates.
*/
class PeekIterator[A](iterator : Iterator[A]) extends Iterator[A] {
  val peekQueue = new Queue[A]()
  def hasNext : Boolean = {
    peekQueue.nonEmpty || iterator.hasNext
  }
  def next : A = {
    if(peekQueue.nonEmpty) {
      peekQueue.dequeue();
    } else {
      iterator.next
    }
  }
  def hasPeekNext = iterator.hasNext

  def peek = {
    val item = iterator.next
    peekQueue.enqueue(item)
  }

  def putBack(item : A) {
    peekQueue.enqueue(item)
  }

  def putBack(items : Iterable[A]) {
    items foreach { peekQueue.enqueue(_)}
  }
}


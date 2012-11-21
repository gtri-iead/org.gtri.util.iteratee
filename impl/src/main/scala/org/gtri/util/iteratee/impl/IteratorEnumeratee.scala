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

import base.BaseEnumeratee
import org.gtri.util.iteratee.api
import api._
import collection.immutable.Iterable

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/15/12
* Time: 11:45 AM
* To change this template use File | Settings | File Templates.
*/
// TODO: replace PeekIterator
class IteratorEnumeratee[A,S](
  peekIterator : PeekIterator[A],
  i : Iteratee[A,S],
  chunkSize : Int = 256,
  outputBuffer : List[A] = Nil,
  outputBufferSize : Int = 0,
  issues : List[Issue]
  ) extends BaseEnumeratee[A,S](i, issues) {

  def attach[T](i: Iteratee[A, T]) = new IteratorEnumeratee(peekIterator, i, chunkSize, outputBuffer, outputBufferSize, issues)

  def isDone = peekIterator.hasNext == false || downstream.isDone

  def step = {
    if(isDone == false) {
      if(outputBuffer.size < chunkSize) {
        val nextItem = fetchNextItem(peekIterator)
        val nextOutputBuffer = nextItem :: outputBuffer
        new IteratorEnumeratee(peekIterator, i, chunkSize, nextOutputBuffer, outputBufferSize + 1, issues)
      } else {
        val nextIteratee = flushOutputBuffer(outputBuffer)
        new IteratorEnumeratee(peekIterator, nextIteratee, chunkSize, Nil, 0, issues)
      }
    } else {
      this
    }
  }

  protected def fetchNextItem(iterator : PeekIterator[A]) : A = {
    iterator.next
  }

  protected def onFullBuffer(buffer : Iterable[A]) : Iterable[A] = {
    buffer
  }

  private def flushOutputBuffer(outputBuffer : List[A]) = {
    val buffer = onFullBuffer(outputBuffer.reverse)
    buffer.foldLeft(downstream) { (i,a) => i(a) }
  }
}

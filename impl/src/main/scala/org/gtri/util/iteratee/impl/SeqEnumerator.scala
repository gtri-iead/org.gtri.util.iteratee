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
import scala.collection.immutable.Seq
import org.gtri.util.iteratee.impl.Enumerators._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
class SeqEnumerator[A](traversable : Seq[A], chunkSize : Int = STD_CHUNK_SIZE) extends Enumerator[A] {
  import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

  abstract class BaseCont[A](current : Seq[A]) extends Enumerators.Cont[A] {
    def step = {
      val (nextChunk, remaining) = current.splitAt(chunkSize)
      if(remaining.isEmpty) {
        Success(progress, nextChunk)
      } else {
        Result(new Cont(remaining), nextChunk)
      }
    }
  }
  /**
   * When traversable has no definite size, can't provide progress
   * @param current
   * @tparam A
   */
  class Cont[A](current : Seq[A]) extends BaseCont[A](current) {
    val progress = Progress.empty
  }

  /**
   * When traversable has definite size, provide progress as enumeration proceeds
   * @param current
   * @tparam A
   */
  class ContWithDefSize[A](current : Seq[A]) extends BaseCont[A](current) {
    val progress = new Progress(0,traversable.size - current.size, traversable.size)
  }

  def initialState() = {
    if (traversable.hasDefiniteSize) {
      new ContWithDefSize(traversable)
    } else {
      new Cont(traversable)
    }
  }
}

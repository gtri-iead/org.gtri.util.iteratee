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

import org.gtri.util.scala.exelog.noop._
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.enumerators._
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

object SeqEnumerator {
  implicit val thisclass = classOf[SeqEnumerator[_]]
  implicit val log : Log = Logger.getLog(thisclass)
}
class SeqEnumerator[A](traversable : Seq[A], chunkSize : Int = STD_CHUNK_SIZE) extends Enumerator[A] {
  require(chunkSize > 0)
  import SeqEnumerator._

  object BaseCont {
    implicit val thisclass = classOf[BaseCont[_]]
    implicit val log : Log = Logger.getLog(thisclass)
  }
  abstract class BaseCont[A](current : Seq[A]) extends enumerators.Cont[A] {
    import BaseCont._
    def step = {
      log.block("step") {
        +"Step enumerator, split by chunkSize"
        val (nextChunk, remaining) = current.splitAt(chunkSize)
        ~s"nextChunk=$nextChunk, remaining=$remaining"
        +"If any remaining then continue otherwise we are done"
        if(remaining.isEmpty) {
          +"Done"
          Success(progress, nextChunk)
        } else {
          +"Continue"
          Result(new Cont(remaining), nextChunk)
        }
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

  object ContWithDefSize {
    implicit val thisclass = classOf[ContWithDefSize[_]]
    implicit val log : Log = Logger.getLog(thisclass)
  }
  /**
   * When traversable has definite size, provide progress as enumeration proceeds
   * @param current
   * @tparam A
   */
  class ContWithDefSize[A](current : Seq[A]) extends BaseCont[A](current) {
    import ContWithDefSize._
    val progress = new Progress(0,traversable.size - current.size, traversable.size)
  }

  def initialState() = {
    log.block("initialState") {
      +"If traversable has definite size then use ContWithDefSize otherwise Cont"
      if (traversable.hasDefiniteSize) {
        +"Traversable has definite size"
        new ContWithDefSize(traversable)
      } else {
        +"Traversable does not have definite size"
        new Cont(traversable)
      }
    }
  }
}

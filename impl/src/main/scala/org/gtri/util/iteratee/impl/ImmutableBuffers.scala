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

import org.gtri.util.iteratee.api
import api.{ImmutableBuffers, ImmutableBuffer}
import scala.collection.JavaConversions._

object ImmutableBufferConversions {
  import scala.language.implicitConversions

  /**
   * Convert an ImmutableBuffer to an IndexedSeq
   * @param buffer
   * @tparam A
   * @return
   */
  implicit def immutableBufferToIndexedSeq[A](buffer : api.ImmutableBuffer[A]) : IndexedSeq[A] = {
    if(buffer.length == 0) {
      IndexedSeq.empty
    } else {
      new IndexedSeq[A] {
        def length = buffer.length

        def apply(idx: Int) = buffer.apply(idx)
      }
    }
  }

  /**
   * Convert a Seq to an ImmutableBuffer
   * @param seq
   * @tparam A
   * @return
   */
  implicit def seqToImmutableBuffer[A](seq : Seq[A]) : api.ImmutableBuffer[A] = {
    if(seq.isEmpty) {
      ImmutableBuffers.empty()
    } else {
      new api.ImmutableBuffer[A] {
        def length = seq.length

        def apply(idx: Int) = seq(idx)

        def iterator = new java.util.Iterator[A] {
          var idx : Int = 0

          def hasNext = idx < seq.length

          def next() = {
            val retv = seq(idx)
            idx += 1
            retv
          }

          def remove = throw new UnsupportedOperationException
        }

        override def toString = seq.toString

        def append(rhs: ImmutableBuffer[A]) = seq ++ rhs

        def slice(start: Int, end: Int) = seq.slice(start,end)
      }
    }
  }
}

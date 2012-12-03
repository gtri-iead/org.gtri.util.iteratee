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
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/2/12
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
object ImmutableBuffers {
  def empty[A] = new api.ImmutableBuffer[A] {
    def length = 0

    def apply(i: Int) = throw new NoSuchElementException

    def iterator = new java.util.Iterator[A] {
      def hasNext = false

      def next() = throw new NoSuchElementException

      def remove = throw new UnsupportedOperationException
    }
  }
  object Conversions {
    implicit def immutableBufferToIndexedSeq[A](buffer : api.ImmutableBuffer[A]) : IndexedSeq[A] = {
      new IndexedSeq[A] {
        def length = buffer.length

        def apply(idx: Int) = buffer.apply(idx)
      }
    }
    implicit def seqToImmutableBuffer[A](seq : Seq[A]) : api.ImmutableBuffer[A] = {
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
      }
    }
  }
}

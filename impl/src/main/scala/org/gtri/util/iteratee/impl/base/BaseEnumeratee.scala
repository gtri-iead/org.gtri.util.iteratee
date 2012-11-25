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
object BaseEnumeratee {
  val STD_CHUNK_SIZE = 256
  object base {
    abstract class Cont[A,S](
      val stream : Stream[A],
      val iteratee : Iteratee[A,S],
      val issues : List[Issue] = Nil,
      val chunkSize : Int = STD_CHUNK_SIZE
    ) extends Enumeratee[A,S] {
      def status = StatusCode.CONTINUE

//      def attach[T](i: Iteratee[A, T]) = new Cont(stream, i, issues, chunkSize)

//      protected final def nextChunk() : (Stream[A], Stream[A]) = {
//        stream.sp
//        val chunk = stream.take(chunkSize)
//        val size = chunk.size
//        (chunk, stream.drop(size))
//      }

//      def step = {
//        val (chunk, nextS, nextI) = doStep()
//        if(nextS.isEmpty) {
//          Success(nextI, issues)
//        } else {
//          new Cont(nextS, iteratee, issues, chunkSize)
//        }
//      }
    }
    class Success[A,S](val iteratee : Iteratee[A,S], val issues : List[Issue]) extends Enumeratee[A,S] {
      def status = StatusCode.SUCCESS

      def attach[T](i: Iteratee[A, T]) = new Success(i, issues)

      def step = this
    }

  }
}

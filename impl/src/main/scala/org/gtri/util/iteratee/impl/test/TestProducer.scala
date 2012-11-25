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

package org.gtri.util.iteratee.impl.test

import org.gtri.util.iteratee.api
import api._
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.base.BaseEnumeratee.base
import org.gtri.util.iteratee.impl.base.BaseEnumeratee

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/12/12
* Time: 4:22 PM
* To change this template use File | Settings | File Templates.
*/
class TestProducer[A](iterable : java.lang.Iterable[A], chunkSize : java.lang.Integer) extends Producer[A] {
  case class Cont[S](_stream : Stream[A], _iteratee : Iteratee[A,S], _issues : List[Issue], _chunkSize : Int) extends base.Cont(_stream, _iteratee, _issues, _chunkSize) {

    def attach[T](i: Iteratee[A, T]) = Cont(stream, i, issues, chunkSize)

    def step = {
      val (chunk, nextS) = stream.splitAt(chunkSize)
      println("producing chunk=" + chunk)
      val nextI = iteratee(chunk.toList)
      if(nextS.isEmpty) {
        Success(nextI, issues)
      } else {
        Cont(nextS, nextI, issues, chunkSize)
      }
    }
  }
  case class Success[S](_iteratee : Iteratee[A,S], _issues : List[Issue]) extends base.Success(_iteratee, _issues)
  def enumeratee[S](i: Iteratee[A,S]) = Cont(iterable.iterator.toStream,i,Nil,chunkSize)
}

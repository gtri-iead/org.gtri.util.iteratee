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

import org.gtri.util.iteratee.impl.Producer
import org.gtri.util.iteratee.impl.Iteratee._
import annotation.tailrec
import java.lang.Iterable
import java.util.Iterator

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/12/12
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
class TestStringProducer(iterable : Iterable[String]) extends Producer[String] {
  def enumerator = new Enumerator[String] {
    def enumerate[V](iteratee: Iteratee[String, V]) = {
      doEnumerate(iterable.iterator, iteratee)
    }
    @tailrec
    def doEnumerate[V](iterator : Iterator[String], iteratee : Iteratee[String,V]) : Iteratee[String, V] = {
      if(iterator.hasNext) {
        val item = iterator.next
        iteratee match {
          case i@Done(_,_) => i
          case Cont(k) => doEnumerate[V](iterator, k(El(List(item))))
        }
      } else {
        iteratee
      }
    }
  }
}

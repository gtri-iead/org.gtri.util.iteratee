/*
    Copyright 2012 Georgia Tech Research Institute

    Author: lance.gatlin@gtri.gatech.edu

    This file is part of org.gtri.util.downstream library.

    org.gtri.util.downstream library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.gtri.util.downstream library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.gtri.util.downstream library. If not, see <http://www.gnu.org/licenses/>.

*/

package org.gtri.util.iteratee.impl.test

import org.gtri.util.iteratee.api
import api._

import java.lang.Iterable
import java.util.Iterator
import org.gtri.util.iteratee.impl.base.BaseEnumeratee


/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/12/12
* Time: 4:22 PM
* To change this template use File | Settings | File Templates.
*/
class TestStringProducer(iterable : Iterable[String]) extends Producer[String] {
  case class Step[S](iterator : Iterator[String], i : Iteratee[String,S]) extends BaseEnumeratee[String,S](i) {

    def isDone = iterator.hasNext == false || downstream.isDone

    def attach[T](j: Iteratee[String,T]) = Step(iterable.iterator, j)

    def step() = {
      val nextS = iterator.next
      println("nextS=" + nextS)
      val nextI = downstream(nextS)
      Step(iterator, nextI)
    }
  }

  def enumeratee[S](i: Iteratee[String,S]) = Step(iterable.iterator, i)
}

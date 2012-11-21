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

import java.lang.Iterable
import java.util.Iterator
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.base.BaseEnumeratee

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/12/12
* Time: 4:22 PM
* To change this template use File | Settings | File Templates.
*/
class TestIntProducer(iterable : Iterable[java.lang.Integer]) extends Producer[java.lang.Integer] {
  case class Step[S](items : Stream[java.lang.Integer], i : Iteratee[java.lang.Integer,S]) extends BaseEnumeratee[java.lang.Integer, S](i) {

    def isDone = items.isEmpty || downstream.isDone

    def attach[T](i: Iteratee[java.lang.Integer,T]) = Step(items, i)

    def step = {
      val head #:: tail = items
      val nextIteratee = downstream(head)
      println("head=" + head)
      Step(tail, nextIteratee)
    }
  }

  def enumeratee[S](i: Iteratee[java.lang.Integer,S]) = Step(iterable.iterator.toStream, i)
}

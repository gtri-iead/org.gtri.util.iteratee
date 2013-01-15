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

import org.gtri.util.iteratee.api.Enumerator
import org.gtri.util.scala.exelog.noop._

/**
* A class to get an Iterator of Enumerator.State.Result for an Enumerator
* @param r
* @param step
* @tparam O
* @tparam R
*/
class EnumeratorIterator[O,R <: Enumerator.State.Result[O]](r : R, step: R => R) extends scala.Iterator[R] {

  var current = r

  def hasNext = current.next.statusCode.isDone == false

  def next() = {
    current = step(current)
    current
  }
}

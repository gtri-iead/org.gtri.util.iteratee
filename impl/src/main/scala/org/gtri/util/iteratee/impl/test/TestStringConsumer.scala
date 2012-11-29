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

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.Consumers._
import org.gtri.util.iteratee.impl.Consumers

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/12/12
* Time: 4:22 PM
* To change this template use File | Settings | File Templates.
*/
class TestStringConsumer(list : java.util.List[String]) extends Consumer[String] {
  case class Step() extends Consumers.Cont[String] {
    def apply(items: Traversable[String]) = {
      println("received=" + items)
      for (item <- items) {
        list.add(item)
      }
      Result(this)
    }

    def endOfInput() = Result(Success())
  }
  def initialState = Step()
}

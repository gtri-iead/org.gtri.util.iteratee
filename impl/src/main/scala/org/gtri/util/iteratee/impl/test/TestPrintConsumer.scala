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

import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.Iteratees._
import org.gtri.util.iteratee.impl.Iteratees
import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/14/12
* Time: 5:10 AM
* To change this template use File | Settings | File Templates.
*/
class TestPrintConsumer[A] extends Iteratee[A, Unit] {
  case class Cont[A]() extends Iteratees.Cont[A, Unit] {
    def apply(items: ImmutableBuffer[A]) = {
      println("received=" + items)
      for (item <- items) {
        println(item)
      }
      Result(this)
    }

    def endOfInput() = Result(Success())
  }

  def initialState = Cont()
}

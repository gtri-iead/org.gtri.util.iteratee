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

import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.Iteratees._
import ImmutableBuffers.Conversions._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 6:16 AM
 * To change this template use File | Settings | File Templates.
 */
class TranslatorF[A,B](f: A => B) extends Iteratee[A,B]{

  class Cont extends Iteratees.Cont[A,B] {
    def apply(input: ImmutableBuffer[A]) = {
      val nextOutput = input.foldLeft(List[B]()) {
        (list,item) => {
           f(item) :: list
        }
      }
      Result(this, nextOutput, ImmutableBuffers.empty)
    }
    def endOfInput = Result(Success())
  }

  def initialState() = new Cont
}

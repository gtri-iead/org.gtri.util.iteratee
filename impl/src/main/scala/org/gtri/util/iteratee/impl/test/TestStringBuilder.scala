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

import org.gtri.util.iteratee.impl.Builder
import org.gtri.util.iteratee.impl.Iteratee._
import org.gtri.util.iteratee.api.Issue

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/12/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
class TestStringBuilder extends Builder[String,String] {
  def iteratee = {
    def step(issues : List[Issue], value : String) : (Input[String]) => Iteratee[String, String] = {
      case El(chunk, newIssues) =>
        val newValue = chunk.foldLeft(value) { (a,b) => a + b }
        Cont(step(newIssues ::: issues, newValue))
      case Empty() =>
        Cont(step(issues, value))
      case EOF() =>
        Success(value,issues, EOF[String])
    }
    Cont(step(Nil, ""))
  }
}

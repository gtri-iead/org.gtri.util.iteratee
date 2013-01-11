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

import collection.immutable.Seq
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.api
import org.gtri.util.scala.exelog.sideeffects.ClassLog

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 1/9/13
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */
package object enumerators {
  val STD_CHUNK_SIZE = 256
  implicit class apiResultMethods[O](self: api.Enumerator.State.Result[O]) {
    def toDebugString : String = {
      new StringBuilder(256)
        .append("Result(next.statusCode=")
        .append(self.next.statusCode)
        .append(",output#=")
        .append(self.output.length)
        .append(",issues#=")
        .append(self.issues.length)
        .append(")").toString
    }
  }
}

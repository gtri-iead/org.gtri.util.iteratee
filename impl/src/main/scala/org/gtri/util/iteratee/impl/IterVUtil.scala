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

import scalaz.IterV
import scalaz.IterV.{Cont, Done}

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/10/12
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
object IterVUtil {

  def flatMap[E,V1,V2](i : IterV[E,V1], f: V1 => IterV[E,V2]) : IterV[E,V2] = {
    i match {
      case Done(x, remaining) =>
        f(x) match {
          case Done(y, _) =>
            Done(y, remaining)
          case Cont(k) =>
            k(remaining)
        }
      case Cont(k) =>
        Cont(in => flatMap[E,V1,V2](k(in),f))
    }
  }

  def map[E,V1, V2](i : IterV[E,V1], f: V1 => V2) : IterV[E,V2] = {
    i match {
      case Done(x, remaining) =>
        Done(f(x), remaining)
      case Cont(k) =>
        Cont(in => map[E,V1,V2](k(in),f))
    }
  }
}

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
package org.gtri.util.iteratee.impl.iteratees

import org.gtri.util.scala.exelog.noop._
import org.gtri.util.iteratee.api.{StatusCode, Iteratee}
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

/**
* Skeleton implementation class for Iteratees that process chunks of input. Derived classes implement only the
* apply for chunks of input.
*
* @tparam I
* @tparam O
*/
abstract class MultiItemCont[I,O] extends Iteratee.State[I,O] {
  def statusCode = StatusCode.CONTINUE

  def apply(item : I)  = apply(Chunk(item))
}

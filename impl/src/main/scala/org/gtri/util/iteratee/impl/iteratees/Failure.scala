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

import org.gtri.util.issue.api.Issue
import org.gtri.util.iteratee.api._

/**
* Convenience class for a failure that is both a Iteratee.State and Iteratee.Result. FSM will stay in this
* state regardless of further input.
* @param output
* @param overflow
* @param issues
* @tparam I
* @tparam O
*/
case class Failure[I,O](
  output : ImmutableBuffer[O] = ImmutableBuffers.empty[O],
  overflow : ImmutableBuffer[I] = ImmutableBuffers.empty[I],
  issues : ImmutableBuffer[Issue] = ImmutableBuffers.empty[Issue]
) extends BaseDone[I,O] with Iteratee.State.Result[I,O] {
  def next = Failure()
  def statusCode = StatusCode.FAILURE
  override def toString : String = this.toDebugString
}
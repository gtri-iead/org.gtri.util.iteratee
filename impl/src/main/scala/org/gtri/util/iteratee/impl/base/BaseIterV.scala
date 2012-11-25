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

package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
object BaseIterV {
  type IterV[A,V] = Iteratee[A, Option[V]]
  object base {
    abstract class BaseCont[A,V](val issues : List[Issue] = Nil) extends IterV[A,V] {
      def state = None

      def isDone = false

      def overflow = Nil
    }

    abstract class Cont[A,V](issues : List[Issue] = Nil) extends BaseCont[A,V](issues) {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A,V](issues : List[Issue] = Nil) extends BaseCont[A,V](issues) {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A,V](val state : Option[V], val issues : List[Issue] = Nil, val overflow : List[A]) extends IterV[A,V] {
      def isDone = true

      def endOfInput = this
    }

    class Success[A,V](state : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,V](state, issues, overflow) {
      def status = StatusCode.SUCCESS

      def apply(items: List[A]) = Success(state, issues, items ::: overflow)
    }
    object Success {
      def apply[A,V](state : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) = new Success(state, issues, overflow)
    }
    class FatalError[A,V](state : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A,V](state, issues, overflow) {
      def status = StatusCode.FATAL_ERROR

      def apply(items: List[A]) = FatalError(state, issues, items ::: overflow)
    }
    object FatalError {
      def apply[A,V](state : Option[V], issues : List[Issue] = Nil, overflow : List[A] = Nil) = new FatalError[A,V](state, issues, overflow)
    }
  }
}

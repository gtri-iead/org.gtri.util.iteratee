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
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
object BaseIterS {
  type IterS[A] = Iteratee[A, Unit]
  object base {
    abstract class BaseCont[A](val issues : List[Issue] = Nil) extends IterS[A] {
      def isDone = false

      def state = ()

      def overflow = Nil
    }

    abstract class Cont[A](issues : List[Issue] = Nil) extends BaseCont[A](issues) {
      def status = StatusCode.CONTINUE
    }

    abstract class RecoverableError[A](issues : List[Issue] = Nil) extends BaseCont[A](issues) {
      def status = StatusCode.RECOVERABLE_ERROR
    }

    abstract class BaseDone[A](val issues : List[Issue] = Nil, val overflow : List[A]) extends IterS[A] {
      def isDone = true

      def state = ()

      def apply(ignore: EndOfInput) = this
    }

    class Success[A](issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A](issues, overflow) {
      def status = StatusCode.SUCCESS

      def apply(item: A) = Success(issues, item :: overflow)
    }
    object Success {
      def apply[A](issues : List[Issue] = Nil, overflow : List[A] = Nil) = new Success(issues, overflow)
    }
    class FatalError[A](issues : List[Issue] = Nil, overflow : List[A] = Nil) extends BaseDone[A](issues, overflow) {
      def status = StatusCode.FATAL_ERROR

      def apply(item: A) = FatalError[A](issues, item :: overflow)
    }
    object FatalError {
      def apply[A](issues : List[Issue] = Nil, overflow : List[A] = Nil) = new FatalError[A](issues, overflow)
    }
  }
}

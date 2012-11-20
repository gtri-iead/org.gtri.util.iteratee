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

import org.gtri.util.iteratee.api
import api.Signals.EndOfInput
import api._
import java.lang.Integer

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/12/12
* Time: 4:23 PM
* To change this template use File | Settings | File Templates.
*/
class TestIntToStringTranslator extends Translator[Integer, String] {
  case class Cont[S](downstream : Iteratee[String,S]) extends Translatee[Integer, String, S] {

    def issues() = Nil

    def status() = StatusCode.CONTINUE

    def attach[T](i: Iteratee[String,T]) = Cont(i)

    def isDone = downstream.isDone

    def state() = downstream.state

    def apply(input : Integer) = {
      val nextI = downstream(input.toString)
      Cont(nextI)
    }

    def apply(eoi: EndOfInput) = {
      val nextI = downstream(eoi)
      Cont(nextI)
    }
  }

  def translatee[S](i: Iteratee[String,S]) = Cont(i)
}

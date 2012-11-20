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

import java.lang.Integer
import org.gtri.util.iteratee.api.Signals.EndOfInput
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.base.BaseIterV.base

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/12/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
class TestIntegerBuilder extends Builder[Integer, Integer] {

  case class Cont(acc: Integer) extends base.Cont[Integer, Integer] {

    def apply(i: Integer) = Cont(acc + 1)

    def apply(eoi: EndOfInput) = base.Success(Some(acc))
  }

  def iteratee = Cont(0)
}

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

import base.BaseTranslatee
import org.gtri.util.iteratee.api.Iteratee
import org.gtri.util.iteratee.api.Signals.EndOfInput

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 6:16 AM
 * To change this template use File | Settings | File Templates.
 */
class TranslateeF[A,B,S](downstream : Iteratee[B,S], f: (A) => B) extends BaseTranslatee[A,B,S](downstream) {

  def apply(item: A) = TranslateeF(downstream(f(item)), f)

  def apply(eoi: EndOfInput) = TranslateeF(downstream(eoi), f)

  def attach[T](i: Iteratee[B, T]) = TranslateeF(i, f)
}
object TranslateeF {
  def apply[A,B,S](downstream : Iteratee[B,S], f: (A) => B) = new TranslateeF(downstream, f)
}
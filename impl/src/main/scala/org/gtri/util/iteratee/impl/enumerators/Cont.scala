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
package org.gtri.util.iteratee.impl.enumerators

import org.gtri.util.iteratee.api.{StatusCode, Enumerator}

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 1/9/13
 * Time: 11:51 PM
 * To change this template use File | Settings | File Templates.
 */
/**
 * A skeleton implementation for an Enumerator.State in the CONTINUE state.
 * @tparam A
 */
abstract class Cont[A] extends Enumerator.State[A] {
  def statusCode = StatusCode.CONTINUE
}


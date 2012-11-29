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

package org.gtri.util.iteratee.api;

import scala.collection.immutable.Traversable;

/**
 * An interface that represents the immutable state of a translator that can
 * translate a buffer of input items into a buffer of output items. 
 * 
 * @author lance.gatlin@gmail.com
 * @param <A> the input type
 * @param <B> the output type
 */
public interface TranslatorState<A,B> extends MachineState<A,Traversable<B>,TranslatorState<A,B>> {
}

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

/**
 * An interface for a producer of output.
 * 
 * @author Lance
 */
public interface Producer<A> {
  <S> Enumeratee<A,S> enumeratee(Iteratee<A,S> downstream);
}

/*
 * Try #1 : See Enumeratee #1 for why this didn't work
public interface Producer<A> {
  <S, I extends Iteratee<A,S>> Enumeratee<A,S,I> enumeratee(I downstream);
}

 */

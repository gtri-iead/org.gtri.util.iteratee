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

import java.util.Iterator;

/**
 * An interface for an immutable buffer of items.
 * 
 * @author lance.gatlin@gmail.com
 */
public interface ImmutableBuffer<A> extends Iterable<A> {
  /**
   * Get the length of the buffer.
   * @return the length of the buffer.
   */
  int length();
  
  /**
   * Get the item at the specified index
   * @param idx zero-based index. must be less than length
   * @return the item at the specified index
   * @throws NoSuchElementException if index is greater than or equal to length
   */
  A apply(int idx);
  
  /**
   * Get an iterator for the items in the buffer
   * @return an iterator for the items in the buffer
   */
  @Override Iterator<A> iterator();
}

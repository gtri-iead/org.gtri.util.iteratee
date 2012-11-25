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

import scala.collection.immutable.List;

/**
 *
 * @author Lance
 */
public interface Iteratee<A,S> {
  StatusCode status();

  List<Issue> issues();
  List<A> overflow();

  S state();
  
  Iteratee<A,S> apply(List<A> a);
  Iteratee<A,S> endOfInput();
}

/* Try #1
 * Minor mods - always work with a chunk (list) of input and get rid of 
 * EndOfInput signal
public interface Iteratee<A,S> {
  StatusCode status();

  List<Issue> issues();

  List<A> overflow();

  boolean isDone();
  S state();
  
  Iteratee<A,S> apply(A a);
  Iteratee<A,S> apply(EndOfInput unused);
}

 */
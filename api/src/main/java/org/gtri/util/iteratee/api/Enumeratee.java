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
 * Try #2
 * @author Lance
 */

public interface Enumeratee<A,S> {
  List<Issue> issues();
  
  StatusCode status();
  
  S state();
  
  Iteratee<A,S> downstream();
  <T> Enumeratee<A,T> attach(Iteratee<A,T> iteratee);
  
  boolean isDone();
  
  Enumeratee<A,S> step();
}

/*
 * Try #1: Using I extends Iteratee<A,?> instead of above to support preserving 
 * the downstream Iteratee derived-type e.g. Translatee 
 * Reverted this because of: 
 * I i = ...
 * I nextI = i.apply(...) // Fails! i.apply returns only base-class Iteratee
 * Renders this change moot 
 
public interface Enumeratee<A,S, I extends Iteratee<A,S>> {
  List<Issue> issues();
  
  StatusCode status();
  
  S state();
  
  I downstream();
  <T, J extends Iteratee<A,T>> Enumeratee<A,T,J> attach(J iteratee);
  
  boolean isDone();
  
  Enumeratee<A,S,I> step();
  Enumeratee<A,S,I> run();  
}
*/

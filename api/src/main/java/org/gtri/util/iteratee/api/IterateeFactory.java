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

import scala.Function1;
import scala.Option;

/**
 *
 * @author Lance
 */
public interface IterateeFactory {
  IssueHandlingCode issueHandlingCode();
  
  Planner createPlanner();
  <A> Producer<A> createProducer(Enumeratee<A> enumeratee);
  <A,S> Consumer<A,S> createConsumer(Iteratee<A,S> iteratee);
  <A,V> Builder<A,V> createBuilder(Iteratee<A,Option<V>> iteratee);
  <A,B> Translator<A,B> createTranslator(Function1<A,B> f);
}

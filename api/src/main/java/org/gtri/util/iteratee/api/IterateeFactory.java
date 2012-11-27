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
 * An interface for a factory that can create implementations of various 
 * interfaces in the iteratee library.
 * 
 * @author Lance
 */
public interface IterateeFactory {
  /**
   * Get the issue handling strategy for this factory.
   * @return the issue handling strategy for this factory
   */
  IssueHandlingCode issueHandlingCode();
  
  /**
   * Create a planner object that utilizes the factory's issue handling 
   * strategy.
   * @return 
   */
  Planner createPlanner();
  /**
   * Create a producer from an enumeratee.
   * 
   * @param <A> the output type
   * @param enumeratee an enumeratee
   * @return a producer whose enumeratee method will return the passed 
   * enumeratee
   */
  <A> Producer<A> createProducer(Enumeratee<A> enumeratee);
  /**
   * Create a consumer from an iteratee
   * @param <A> the input type
   * @param <S> the state type
   * @param iteratee an iteratee
   * @return a consumer whose iteratee method will return the passed iteratee
   */
  <A,S> Consumer<A,S> createConsumer(Iteratee<A,S> iteratee);
  /**
   * Create a builder from a builder iteratee
   * @param <A> the input type
   * @param <V> the value type
   * @param iteratee a builder iteratee
   * @return a builder whose iteratee method will return the passed iteratee
   */
  <A,V> Builder<A,V> createBuilder(Iteratee<A,Option<V>> iteratee);
  /**
   * Create a simple stateless translator from a function
   * @param <A> the input type
   * @param <B> the output type
   * @param f a function that converts an item of the input type to an item of 
   * the output type
   * @return a translator that converts items of the input type to items of the
   * output type
   */
  <A,B> Translator<A,B> createTranslator(Function1<A,B> f);
}

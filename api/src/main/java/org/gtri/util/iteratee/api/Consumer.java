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

import scala.Unit;
import scala.collection.immutable.Traversable;

/**
 * An interface for a consumer of the output of a producer that causes 
 * side-effects
 * 
 * @param <A> the input type
 * @author lance.gatlin@gmail.com
 */
public interface Consumer<A> extends Machine<A,Unit,Consumer.State<A>> {
  /**
   *
   * @author Lance
   */
  public static interface State<A> extends Machine.State<A,Unit,Consumer.State<A>> {
  }
  /**
   * An interface for a plan to stream input from a producer to a consumer
   * 
   * @author lance.gatlin@gmail.com
   */
  public static interface Plan<A,B> {
    /**
     * Get the producer for the plan
     *
     * @return a producer for the plan
     */
    Producer<A> producer();

    /**
     * Get the consumer for the plan
     *
     * @return a consumer for the plan
     */
    Consumer<B> consumer();
    /**
     * Run the plan to get results
     * 
     * @return results
     */
    Result<A,B> run();
  }
  
  /**
   * An interface for the immutable result of running a plan
   * @param <A> the input type
   * @param <B> the output type
   */
  public static interface Result<A,B> {
    StatusCode status();

    Traversable<Issue> issues();

    Traversable<B> overflow();
    
    /**
     * Get the producer after processing
     *
     * @return the producer after processing
     */
    Producer<A> producer();

    /**
     * Get the builder after processing
     *
     * @return the builder after processing
     */
    Consumer<B> consumer();
    
  }
  /**
   * Get the initial state of the consumer
   * @return the initial state of the consumer
   */
  @Override
  Consumer.State<A> initialState();
}

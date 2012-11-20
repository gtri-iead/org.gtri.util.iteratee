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
 * An interface for a consumer of the output of a producer.
 * 
 * @author Lance
 */
public interface Consumer<A> {
  public static interface State<A> {
    StatusCode status();
    
    List<Issue> issues();
  
    List<A> overflow();
  }
    
  /**
   * An interface for a plan to stream input from a producer to a consumer
   * 
   * @author Lance
   */
  public static interface Plan<A> {
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
    Consumer<A> consumer();
    /**
     * Run the plan to get results
     * 
     * @return results
     */
    Result<A> run();


  }
  /**
   * The results of running a plan
   *
   * @param <A> the input/output type
   */
  public static interface Result<A> extends State<A> {
    /**
     * Get the producer after processing
     *
     * @return the producer after processing
     */
    Producer<A> producer();

    /**
     * Get the consumer after processing
     *
     * @return the consumer after processing
     */
    Consumer<A> consumer();
  }
  
  Iteratee<A,Consumer.State<A>> iteratee();
}

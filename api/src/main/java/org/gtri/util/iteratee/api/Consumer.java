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

import java.util.List;

/**
 * An interface for a consumer of the output of a producer.
 * 
 * @author Lance
 */
public interface Consumer<A> {
  /**
   * A plan that can be run to for results.
   * 
   * @param <A> the input/output type
   */
  public static interface Plan<A> {
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
  public static interface Result<A> {
    /**
     * Get the success/failure status of the execution
     * @return TRUE if run succeeded FALSE otherwise
     */
    boolean isSuccess();
    /**
     * Get a list of issues identified during processing
     * 
     * @return a list of issues identified during processing
     */
    List<Issue> getIssues();
    
    /**
     * Get a producer that represents the state of the producer after execution.
     * @return a producer that represents the state of the producer after execution.
     */
    Producer<A> getProducer();
    /**
     * Get a consumer that represents the state of the producer after execution.
     * @return a consumer that represents the state of the producer after execution.
     */
    Consumer<A> getConsumer();
  }
}

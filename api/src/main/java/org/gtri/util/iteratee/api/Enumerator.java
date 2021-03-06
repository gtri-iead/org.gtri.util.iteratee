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

import org.gtri.util.issue.api.Issue;

/**
 * An interface for an Enumerator of output items. An Enumerator is a Moore 
 * machine, a finite state machine whose output is determined solely by its 
 * current state. 
 * 
 * @author lance.gatlin@gmail.com
 * @param <O> the output type
 */
public interface Enumerator<O> {
  
  /**
   * Get the initial state of the Enumerator
   * @return the initial state of the Enumerator
   */
  Enumerator.State<O> initialState();

  
  /**
   * An interface for an immutable state of a Moore machine. An output 
   * item is generated by an Enumerator with each successive call to its step 
   * method. An Enumerator signals completion (or the presence of errors) by way 
   * of the status method. Further calls to the step method after an 
   * Enumerator's status method has indicated completion are a noop and are 
   * ignored.
   * 
   * Note1: Enumerator implementations should not throw exceptions. Instead,
   * exceptions should be returned as issues in the result and the 
   * status of the next Enumerator set to FATAL_ERROR should processing be 
   * stopped or RECOVERABLE_ERROR if processing may continue.
   * 
   * @author lance.gatlin@gmail.com
   * @param <O> the output type
   */
  public static interface State<O> {
    /**
     * Get the status code of the Enumerator
     * @return status code of the Enumerator
     */
    StatusCode statusCode();

    /**
     * Get the progress of the Enumerator
     * @return the progress of the Enumerator
     */
    Progress progress();
    
    /**
     * Increment the Enumerator
     * @return an immutable result object
     */  
    Result<O> step();
    
    /**
     * The immutable result of incrementing an Enumerator
     * 
   * @param <O> the output type
     */  
    public static interface Result<O> {
      /**
       * Get the next immutable state of the Enumerator
       * @return the next immutable state of the Enumerator
       */
      Enumerator.State<O> next();
      /**
       * Get a buffer of output items
       * @return the buffer of output items
       */
      ImmutableBuffer<O> output();
      /**
       * Get any issues raised during processing
       * @return any issues raised during processing
       */
      ImmutableBuffer<Issue> issues();
    }

  }
  
}

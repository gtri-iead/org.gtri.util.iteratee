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
 * An interface for an Iteratee. An Iteratee is a Mealy machine, a finite state 
 * machine whose output is determined by both its current state and its input. 
 * 
 * @author lance.gatlin@gmail.com
 * @param <I> the input type
 * @param <O> the output type
 */
public interface Iteratee<I,O> {
  /**
   * Get the initial state of the Iteratee
   * @return the initial state of the Iteratee
   */
  Iteratee.State<I,O> initialState();
  
  /**
   * An interface for an immutable state of an Iteratee. Applying a buffer 
   * of input to the Iteratee returns an immutable result object that contains 
   * the next immutable Iteratee state, the output, any overflow (unused input) 
   * and any issues raised during processing. An Iteratee signals completion (or 
   * the presence of errors) by way of its status method. Further calls to the 
   * apply method after an Iteratee's status method has indicated completion are 
   * a noop and will result in all applied input being returned as overflow in 
   * the result. Once all input has been applied to an Iteratee, the endOfInput 
   * method must be called to signal to the Iteratee to move to the SUCCESS or 
   * FATAL_ERROR status.
   * 
   * Note1: Iteratee.State implementations should not throw exceptions. Instead,
   * exceptions should be returned as issues in the result and the 
   * status of the next Iteratee set to FATAL_ERROR should processing be stopped
   * or RECOVERABLE_ERROR if processing may continue.
   * 
   * Note2: Application of input to an Iteratee may result in more overflow than 
   * input applied in the method call. Earlier Iteratee.States may internally 
   * cache input that is then returned by a later Iteratee state.
   * 
   * @author lance.gatlin@gmail.com
   * @param <I> the input type
   * @param <O> the output type
   */
  public static interface State<I,O> {
    /**
     * Get the status of the Iteratee
     * @return status of the Iteratee
     */
    StatusCode statusCode();

    /**
     * Apply input to the Iteratee
     * @param input 
     * @return an immutable result object
     */  
    Iteratee.State.Result<I,O> apply(ImmutableBuffer<I> input);
    
    /**
     * Apply input to the Iteratee
     * @param input 
     * @return an immutable result object
     */  
    Iteratee.State.Result<I,O> apply(I input);
    
    /**
     * Signal to the Iteratee the end of input
     * @return an immutable result object
     */
    Iteratee.State.Result<I,O> endOfInput();
    
    /**
     * The immutable result of applying input to an Iteratee
     * 
     * @param <I> the input type
     * @param <O> the output type
     */  
    public static interface Result<I,O> {
      /**
       * Get the next immutable state of the Iteratee
       * @return the next immutable state of the Iteratee
       */
      Iteratee.State<I,O> next();
      /**
       * Get the output
       * @return the output
       */
      ImmutableBuffer<O> output();
      /**e
       * Get any unused input. May return input cached by previous states.
       * @return any unused input
       */
      ImmutableBuffer<I> overflow();
      /**
       * Get issues raised during processing
       * @return issues raised during processing
       */
      ImmutableBuffer<Issue> issues();
    }
  }
  
}

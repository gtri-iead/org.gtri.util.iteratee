/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.collection.immutable.Traversable;

/**
 * An interface for a Moore machine. A Moore machine is a finite state machine 
 * whose output is determined solely by its current state. 
 * 
 * @author lance.gatlin@gmail.com
 * @param <O> the output type
 */
public interface Enumerator<O> {
  /**
   * Get the initial state of the enumerator
   * @return the initial state of the enumerator
   */
  Enumerator.State<O> initialState();

  /**
   * An interface for an immutable state of a Moore machine. An output 
   * item is generated by an Enumerator with each successive call to its step 
   * method. An Enumerator signals completion (or the presence of errors) by way 
   * of the status method. Further calls to the step method after an Enumerator's 
   * status method has indicated completion are a noop. 
   * 
   * Note1: Enumerator implementations should not throw exceptions. Instead,
   * exceptions should be returned as issues in the result and the 
   * status of the next Enumerator set to FATAL_ERROR (should processing stop) or 
   * RECOVERABLE_ERROR (if processing may continue).
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
      Traversable<O> output();
      /**
       * Get any issues raised during processing
       * @return any issues raised during processing
       */
      Traversable<Issue> issues();
    }

  }
  
}

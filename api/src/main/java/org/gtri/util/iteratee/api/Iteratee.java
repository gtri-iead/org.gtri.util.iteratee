/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.collection.immutable.Traversable;

/**
 * An interface for a Mealy machine. A Mealy machine is a finite state machine 
 * whose output is determined by both its current state and its input. The 
 * machine interface is used to obtain the initial state of the machine. Input
 * may then be applied to the state to obtain further states. See Machine.State
 * below.
 * 
 * @author lance.gatlin@gmail.com
 * @param <I> the input type
 * @param <O> the output type
 */
public interface Iteratee<I,O> {
  /**
   * Get the initial state of the Machine
   * @return the initial state of the Machine
   */
  Iteratee.State<I,O> initialState();
  
  /**
   * An interface for an immutable state of a Mealy machine. Applying a buffer 
   * of input to the Machine returns an immutable result object that contains 
   * the next immutable Machine state, the output, any overflow (unused input) 
   * and any issues raised during processing. A Machine signals completion (or 
   * the presence of errors) by way of the status method. Further calls to the 
   * apply method after a Machine's status method has indicated completion are a 
   * noop and will result in all applied input being returned as overflow in the 
   * result. Once all input has been applied to a Machine, the endOfInput method 
   * must be called to signal to the Machine to move to a completion state 
   * (either SUCCESS or FATAL_ERROR).
   * 
   * Note1: Machine.State implementations should not throw exceptions. Instead,
   * exceptions should be returned as issues in the result and the 
   * status of the next Machine set to FATAL_ERROR (should processing stop) or 
   * RECOVERABLE_ERROR (if processing may continue).
   * 
   * Note2: Application of input to a Machine may result in more overflow than 
   * input applied in the method call. Earlier Machine.States may internally 
   * cache input that is then returned by a later Machine state.
   * 
   * @author lance.gatlin@gmail.com
   * @param <I> the input type
   * @param <O> the output type
   */
  public static interface State<I,O> {
    /**
     * Get the status of the Machine
     * @return status of the Machine
     */
    StatusCode statusCode();

    /**
     * Apply input to the Machine
     * @param input 
     * @return an immutable result object
     */  
    Iteratee.State.Result<I,O> apply(Traversable<I> input);
    /**
     * Signal to the Machine the end of input
     * @return an immutable result object
     */
    Iteratee.State.Result<I,O> endOfInput();
    
    /**
     * The immutable result of applying input to an Machine
     * 
     * @param <I> the input type
     * @param <O> the output type
     */  
    public static interface Result<I,O> {
      /**
       * Get the next immutable state of the Machine
       * @return the next immutable state of the Machine
       */
      Iteratee.State<I,O> next();
      /**
       * Get the output
       * @return the output
       */
      Traversable<O> output();
      /**e
       * Get any unused input. May return input cached by previous states.
       * @return any unused input
       */
      Traversable<I> overflow();
      /**
       * Get issues raised during processing
       * @return issues raised during processing
       */
      Traversable<Issue> issues();
    }
  }
  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.collection.immutable.Traversable;

/**
 * An interface for an immutable state of a Mealy machine (MM). An MM is a 
 * finite state machine whose output is determined by both its current state and 
 * its input. Applying input to the MM returns an immutable result object that
 * contains the next immutable MM state, the output, any overflow (unused input)
 * and any issues raised during processing. A MM signals completion (or the 
 * presence of errors) by way of the status method. Further calls to the apply 
 * method after an MM's status method has indicated completion are a noop and 
 * will result in all applied input being returned as overflow in the result. 
 * Additionally, once all input has been applied to an MM, the endOfInput method 
 * must be called to signal to the MM to move to a completion status (either 
 * SUCCESS or FATAL_ERROR).
 * 
 * Note1: MM implementations should not throw exceptions. Instead,
 * exceptions should be returned as issues in the result and the 
 * status of the next MM set to FATAL_ERROR (should processing stop) or 
 * RECOVERABLE_ERROR (if processing may continue).
 * 
 * @author Lance
 * @param <I> the input type
 * @param <O> the output type
 * @param <M> the most derived type of the MM
 */
public interface MealyMachine<I,O,M> {
  /**
   * Get the status of the MM
   * @return status of the MM
   */
  StatusCode status();
  
  /**
   * The immutable result of applying input to an MM
   * 
   * @param <I> the input type
   * @param <O> the output type
   * @param <M> the most derived type of the MM
   */  
  public interface Result<I,O,M> {
    /**
     * Get the next immutable state of the MM
     * @return the next immutable state of the MM
     */
    M next();
    /**
     * Get the output
     * @return the output
     */
    O output();
    /**
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
  
/**
   * Apply input to the MM
   * @param input 
   * @return an immutable result object
   */  
  Result<I,O,M> apply(I input);
  /**
   * Signal to the MM the end of input
   * @return an immutable result object
   */
  Result<I,O,M> endOfInput();
}

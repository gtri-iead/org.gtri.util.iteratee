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

import java.util.Iterator;

/**
 * An interface for a plan to stream items from an Enumerator to an Iteratee.
 * 
 * @author lance.gatlin@gmail.com
 */
public interface Plan2<I, O> extends Enumerator<O>, Iterable<Plan2.State.Result<I,O>> {
  
  /**
   * Get the factory that was used to create this plan
   * @return the factory that was used to create this plan
   */
  IterateeFactory factory();
  
  /**
   * Get the Enumerator 
   * @return the Enumerator
   */
  Enumerator<I> enumerator();

  /**
   * Get the Iteratee
   * @return the Iteratee
   */
  Iteratee<I, O> iteratee();

  /**
   * Get an iterator that allows stepping through the results of the plan using
   * the factory's default IssueHandlingCode. Note: does *not* call endOfInput 
   * for the Iteratee.
   * @return an iterator that allows stepping through the results of the plan 
   * using the factory's default IssueHandlingCode
   */
  @Override Iterator<Plan2.State.Result<I,O>> iterator();
  
  /**
   * Get an iterator that allows stepping through the results of the plan using
   * the supplied IssueHandlingCode. Note: does *not* call endOfInput for the 
   * Iteratee.
   * @param issueHandlingCode
   * @return an iterator that allows stepping through the results of the plan 
   * using the supplied IssueHandlingCode
   */
  Iterator<Plan2.State.Result<I,O>> iterator(IssueHandlingCode issueHandlingCode);
  
  /**
   * Run the plan to completion, including calling endOfInput on the Iteratee
   * after exhausting the Enumerator's output
   * @return the result of running the plan to completion
   */
  Plan2.RunResult<I,O> run();
  
  /**
   * An immutable run result
   * @param <I> the input type
   * @param <O> the output type
   */
  public static interface RunResult<I,O> {
    /**
     * The result of the plan immediately before the Iteratee's endOfInput 
     * method was called.
     * 
     * @return result of the plan immediately before the endOfInput method was 
     * called.
     */
    Plan2.State.Result<I,O> lastResult();
    
    /**
     * The final result of the plan, after the Iteratee's endOfInput method was
     * called
     * @return final result of the plan
     */
    Plan2.State.Result<I,O> endOfInput();
            
    /**
     * The final status of the completed run
     * @return final status of the completed run
     */
    StatusCode statusCode();

    /**
     * The final progress of the completed plan
     * @return final progress of the completed plan
     */
    Progress progress();
    
    /**
     * An Enumerator whose initialState is the state of the Enumerator in the 
     * lastResult. Equivalent to 
     * factory.createEnumerator(lastResult().enumeratorState())
     * @return Enumerator whose initialState is the state of the Enumerator in the 
     * lastResult
     */
    Enumerator<I> enumerator();

    /**
     * An Iteratee whose initialState is the state of the Iteratee in the 
     * lastResult (before the endOfInput method was called). Equivalent to
     * factory.createIteratee(lastResult().iterateeState())
     * @return 
     */
    Iteratee<I, O> iteratee();
    
    /**
     * Get a buffer containing the output of all results
     * @return a buffer containing the output of all results
     */
    ImmutableBuffer<O> allOutput();
    /**
     * Get a buffer containing the output of all issues
     * @return a buffer containing the output of all issues
     */
    ImmutableBuffer<Issue> allIssues();
  }
  
  /**
   * Get the initial state of the plan
   * @return the initial state of the plan
   */
  @Override Plan2.State<I,O> initialState();

  /**
   * An interface for the immutable state of the plan.
   * @param <I> the input type
   * @param <O> the output type
   */
  public static interface State<I,O> extends Enumerator.State<O> {
    /**
     * Increment the plan 
     * @return an immutable result 
     */
    @Override Plan2.State.Result<I,O> step();
    
    /**
     * Call endOfInput on the Iteratee. Equivalent to 
     * iterateeState().endOfInput()
     * @return an immutable result for calling endOfInput on the Iteratee
     */
    Plan2.State.Result<I,O> endOfInput();
    
    /**
     * Get the state of the Enumerator
     * @return the state of the Enumerator
     */
    Enumerator.State<I> enumeratorState();

    /**
     * Get the state of the Iteratee
     * @return the state of the Iteratee
     */
    Iteratee.State<I, O> iterateeState();
      
    /**
     * An interface for the immutable result of incrementing the state of the 
     * plan.
     */
    public static interface Result<I, O> extends Enumerator.State.Result<O> {
      /**
       * Get the next state of the plan
       * @return the next state of the plan
       */
      @Override Plan2.State<I,O> next();
      
      /**
       * Get a buffer containing any items not used by the Iteratee
       * @return a buffer containing any items not used by the Iteratee
       */
      ImmutableBuffer<I> overflow();
    }
  }
  
}

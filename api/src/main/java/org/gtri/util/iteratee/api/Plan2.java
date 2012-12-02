/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.collection.immutable.Traversable;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public interface Plan2<I, O> extends Enumerator<O> {

  Enumerator<I> enumerator();

  Iteratee<I, O> iteratee();

  Plan2.RunResult<I, O> run();
  
  public static interface RunResult<I,O> {
    Progress progress();
    StatusCode statusCode();
    Traversable<Traversable<O>> allOutput();
    Traversable<I> overflow();
    Traversable<Issue> allIssues();
    Enumerator<I> enumerator();
    Iteratee<I, O> iteratee();
  }
  
  @Override
  Plan2.State<I,O> initialState();

  public static interface State<I,O> extends Enumerator.State<O> {
    @Override
    Plan2.State.Result<I,O> step();
    
    Enumerator.State<I> enumeratorState();

    Iteratee.State<I, O> iterateeState();
      
    public static interface Result<I, O> extends Enumerator.State.Result<O> {
      @Override
      Plan2.State<I,O> next();
    }
  }
  
}

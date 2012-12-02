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
public interface Plan3<I1,I2, O> extends Enumerator<O> {

  Enumerator<I1> enumerator();

  Iteratee<I1, I2> translator();
  
  Iteratee<I2, O> iteratee();

  RunResult<I1,I2, O> run();
  
  public static interface RunResult<I1,I2,O> {
    Status status();
    Traversable<Traversable<O>> allOutput();
    Traversable<I2> overflow();
    Traversable<Issue> allIssues();
    Enumerator<I1> enumerator();
    Iteratee<I1,I2> translator();
    Iteratee<I2, O> iteratee();
  }
  
  @Override
  Plan3.State<I1,I2,O> initialState();

  public static interface State<I1,I2,O> extends Enumerator.State<O> {
    @Override
    Plan3.State.Result<I1,I2,O> step();
    
    Enumerator.State<I1> enumeratorState();

    Iteratee.State<I1, I2> translatorState();

    Iteratee.State<I2, O> iterateeState();
    
    public static interface Result<I1,I2, O> extends Enumerator.State.Result<O> {
      @Override
      Plan3.State<I1,I2,O> next();
    }
  }
  
}

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
 *
 * @author lance.gatlin@gmail.com
 */
public interface Plan2<I, O> extends Enumerator<O>, Iterable<Plan2.State.Result<I,O>> {

  
  Enumerator<I> enumerator();

  Iteratee<I, O> iteratee();

  Iterator<Plan2.State.Result<I,O>> iterator();
  Iterator<Plan2.State.Result<I,O>> iterator(IssueHandlingCode issueHandlingCode);
//  <U> U runFoldLeft(U u, Function2<U, Plan2.State.Result<I,O>,U> f);
//  void runForEach(Function1<Plan2.State.Result<I,O>,?> f);
  Plan2.RunResult<I,O> run();
  
  public static interface RunResult<I,O> {
    Plan2.State.Result<I,O> lastResult();
    
    Plan2.State.Result<I,O> endOfInput();
            
    StatusCode statusCode();

    Progress progress();
    
    Enumerator<I> enumerator();

    Iteratee<I, O> iteratee();
    
    ImmutableBuffer<O> allOutput();
    ImmutableBuffer<Issue> allIssues();
  }
  
  @Override
  Plan2.State<I,O> initialState();

  public static interface State<I,O> extends Enumerator.State<O> {
    @Override
    Plan2.State.Result<I,O> step();
    
    Plan2.State.Result<I,O> endOfInput();
    
    Enumerator.State<I> enumeratorState();

    Iteratee.State<I, O> iterateeState();
      
    public static interface Result<I, O> extends Enumerator.State.Result<O> {
      @Override
      Plan2.State<I,O> next();
      
      ImmutableBuffer<I> overflow();
    }
  }
  
}

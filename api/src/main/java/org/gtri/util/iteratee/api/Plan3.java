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
public interface Plan3<I1,I2, O> extends Enumerator<O>, Iterable<Plan3.State.Result<I1,I2,O>> {

  Enumerator<I1> enumerator();

  Iteratee<I1, I2> translator();
  
  Iteratee<I2, O> iteratee();

  @Override Iterator<Plan3.State.Result<I1,I2,O>> iterator();
  
  RunResult<I1,I2,O> run();
  
  public static interface RunResult<I1,I2,O> {
    Plan3.State.Result<I1,I2,O> lastResult();
    
    Plan3.State.Result<I1,I2,O> endOfInput();
            
    StatusCode statusCode();

    Progress progress();
    
    Enumerator<I1> enumerator();

    Iteratee<I1, I2> translator();

    Iteratee<I2, O> iteratee();
    
    ImmutableBuffer<O> allOutput();
    ImmutableBuffer<Issue> allIssues();
  }
  
  @Override
  Plan3.State<I1,I2,O> initialState();

  public static interface State<I1,I2,O> extends Enumerator.State<O> {
    @Override
    Plan3.State.Result<I1,I2,O> step();
    
    Plan3.State.Result<I1,I2,O> endOfInput();
            
    Enumerator.State<I1> enumeratorState();

    Iteratee.State<I1, I2> translatorState();

    Iteratee.State<I2, O> iterateeState();
    
    public static interface Result<I1,I2,O> extends Enumerator.State.Result<O> {
      @Override
      Plan3.State<I1,I2,O> next();
      
      ImmutableBuffer<I2> overflow();      
    }
  }
  
}

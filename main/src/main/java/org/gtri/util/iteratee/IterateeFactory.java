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

package org.gtri.util.iteratee;

import org.gtri.util.iteratee.api.Builder;
import org.gtri.util.iteratee.api.Builder.State;
import org.gtri.util.iteratee.api.Consumer;
import org.gtri.util.iteratee.api.Enumeratee;
import org.gtri.util.iteratee.api.IssueHandlingCode;
import org.gtri.util.iteratee.api.Iteratee;
import org.gtri.util.iteratee.api.Planner;
import org.gtri.util.iteratee.api.Producer;
import org.gtri.util.iteratee.api.Translator;
import scala.Function1;

/**
 *
 * @author Lance
 */
public class IterateeFactory implements org.gtri.util.iteratee.api.IterateeFactory {
  
  private final IssueHandlingCode errorHandlingCode;
  
  public IterateeFactory() {
    this(IssueHandlingCode.NORMAL);
  }
  public IterateeFactory(final IssueHandlingCode errorHandlingCode) {
    this.errorHandlingCode = errorHandlingCode;
  }
  
  @Override
  public IssueHandlingCode issueHandlingCode() {
    return errorHandlingCode;
  }

  @Override
  public Planner createPlanner() {
    return new org.gtri.util.iteratee.impl.Planner(this);
  }

  @Override
  public <A, S> Producer<A> createProducer(final Enumeratee<A, S> e) {
    return new Producer<A>() {

      @Override
      public <S> Enumeratee<A, S> enumeratee(final Iteratee<A, S> i) {
        return e.attach(i);
      }
      
    };
  }

  @Override
  public <A, S> Consumer<A, S> createConsumer(final Iteratee<A, S> i) {
    return new Consumer<A,S>() {

      @Override
      public Iteratee<A, S> iteratee() {
        return i;
      }
      
    };
  }

  @Override
  public <A, V> Builder<A, V> createBuilder(final Iteratee<A, State<V>> i) {
    return new Builder<A,V>() {

      @Override
      public Iteratee<A, State<V>> iteratee() {
        return i;
      }
    
    };
  }

  @Override
  public <A, B> Translator<A, B> createTranslator(final Function1<A, B> f) {
    return new Translator<A,B>() {

      @Override
      public <S> Iteratee<A, S> translatee(final Iteratee<B, S> i) {
        return new org.gtri.util.iteratee.impl.TranslateeF(i, f);
      }
      
    };
  }
  
}

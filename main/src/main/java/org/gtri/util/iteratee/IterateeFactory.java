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
import org.gtri.util.iteratee.api.BuilderState;
import org.gtri.util.iteratee.api.Consumer;
import org.gtri.util.iteratee.api.ConsumerState;
import org.gtri.util.iteratee.api.IssueHandlingCode;
import org.gtri.util.iteratee.api.Iteratee;
import org.gtri.util.iteratee.api.IterateeState;
import org.gtri.util.iteratee.api.Planner;
import org.gtri.util.iteratee.api.Producer;
import org.gtri.util.iteratee.api.ProducerState;
import org.gtri.util.iteratee.api.Translator;
import scala.Function1;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class IterateeFactory implements org.gtri.util.iteratee.api.Factory {
  
  private final IssueHandlingCode issueHandlingCode;
  
  public IterateeFactory() {
    this(IssueHandlingCode.NORMAL);
  }
  public IterateeFactory(final IssueHandlingCode errorHandlingCode) {
    this.issueHandlingCode = errorHandlingCode;
  }
  
  @Override
  public IssueHandlingCode issueHandlingCode() {
    return issueHandlingCode;
  }

  @Override
  public Planner createPlanner() {
    return new org.gtri.util.iteratee.impl.Planner(this);
  }

  @Override
  public <A> Producer<A> createProducer(final ProducerState<A> p) {
    return new Producer<A>() {

      @Override
      public ProducerState<A> initialState() {
        return p;
      }

      
    };
  }

  @Override
  public <A> Consumer<A> createConsumer(final ConsumerState<A> c) {
    return new Consumer<A>() {

      @Override
      public ConsumerState<A> initialState() {
        return c;
      }

      
    };
  }

  @Override
  public <A,S> Iteratee<A,S> createIteratee(final IterateeState<A,S> i) {
    return new Iteratee<A,S>() {

      @Override
      public IterateeState<A,S> initialState() {
        return i;
      }

      
    };
  }
  
  @Override
  public <A, V> Builder<A, V> createBuilder(final BuilderState<A,V> b) {
    return new Builder<A,V>() {

      @Override
      public BuilderState<A, V> initialState() {
        return b;
      }

    
    };
  }

  @Override
  public <A, B> Translator<A, B> createTranslator(final Function1<A, B> f) {
    return new org.gtri.util.iteratee.impl.translate.TranslatorF<A,B>(f);
  }
  
}

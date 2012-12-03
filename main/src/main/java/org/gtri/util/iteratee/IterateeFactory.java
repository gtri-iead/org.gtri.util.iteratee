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

import org.gtri.util.iteratee.api.IssueHandlingCode;
import org.gtri.util.iteratee.api.Iteratee;
import org.gtri.util.iteratee.api.Enumerator;
import org.gtri.util.iteratee.api.Plan2;
import org.gtri.util.iteratee.api.Plan3;
import scala.Function1;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class IterateeFactory implements org.gtri.util.iteratee.api.IterateeFactory {
  
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
  public <A> Enumerator<A> createEnumerator(final Enumerator.State<A> p) {
    return new Enumerator<A>() {

      @Override
      public Enumerator.State<A> initialState() {
        return p;
      }

      
    };
  }

  @Override
  public <I,O> Iteratee<I,O> createIteratee(final Iteratee.State<I,O> iteratee) {
    return new Iteratee<I,O>() {

      @Override
      public Iteratee.State<I,O> initialState() {
        return iteratee;
      }

      
    };
  }

//  @Override
//  public <A, B> Iteratee<A, B> createTranslator(final Function1<A, B> f) {
//    return new org.gtri.util.iteratee.impl.TranslatorF<A,B>(f);
//  }

  @Override
  public <I, O> Plan2<I, O> createPlan(Enumerator<I> enumerator, Iteratee<I, O> iteratee) {
    return new org.gtri.util.iteratee.impl.Plan2(this, enumerator, iteratee);
  }

  @Override
  public <I1, I2, O> Plan3<I1, I2, O> createPlan(Enumerator<I1> enumerator, Iteratee<I1, I2> translator, Iteratee<I2, O> iteratee) {
    return new org.gtri.util.iteratee.impl.Plan3(this, enumerator, translator, iteratee);
  }

  @Override
  public <A, B, C> Iteratee<A, C> compose(Iteratee<A, B> first, Iteratee<B, C> second) {
    return new org.gtri.util.iteratee.impl.IterateeSerialTuple2(first,second);
  }

  @Override
  public <A, B, C, D> Iteratee<A, D> compose(Iteratee<A, B> first, Iteratee<B, C> second, Iteratee<C, D> third) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <A, B, C, D, E> Iteratee<A, E> compose(Iteratee<A, B> first, Iteratee<B, C> second, Iteratee<C, D> third, Iteratee<D, E> fourth) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <A, B, C, D, E, F> Iteratee<A, F> compose(Iteratee<A, B> first, Iteratee<B, C> second, Iteratee<C, D> third, Iteratee<D, E> fourth, Iteratee<E, F> fifth) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
 
  
}

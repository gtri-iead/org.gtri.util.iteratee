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

/**
 * An interface for a factory that can create implementations of various 
 * interfaces in the iteratee library.
 * 
 * @author lance.gatlin@gmail.com
 */
public interface IterateeFactory {
  /**
   * Get the issue handling strategy for this factory.
   * @return the issue handling strategy for this factory
   */
  IssueHandlingCode issueHandlingCode();
  
  /**
   * Create a producer from a producer state.
   * 
   * @param <A> the output type
   * @param state state of some producer
   * @return a producer whose initialState method will return the provided
   * state
   */
  <A> Enumerator<A> createEnumerator(Enumerator.State<A> state);
  
  /**
   * Create an iteratee from an iteratee state.
   * @param <A> the input type
   * @param <S> the loop state type
   * @param state state of some iteratee
   * @return an iteratee whose initialState method will return the provided
   * state
   */
  <I,O> Iteratee<I,O> createIteratee(Iteratee.State<I,O> state);
  
//  /**
//   * Create a simple stateless translating iteratee from a function
//   * 
//   * @param <A> the input type
//   * @param <B> the output type
//   * @param f a function that converts an item of the input type to an item of 
//   * the output type
//   * @return an iteratee that converts items of the input type to items of the
//   * output type
//   */
//  <A,B> Iteratee<A,B> createTranslator(Function1<A,B> f);
  
  /**
   * Create a plan to stream items from a producer to an iteratee.
   * 
   * @param <I> the enumerator's output type
   * @param <O> the iteratee's output type
   * @param enumerator
   * @param iteratee
   * @return a plan that can be run to obtain results
   */
  <I,O> Plan2<I,O> createPlan(Enumerator<I> enumerator, Iteratee<I,O> iteratee);
  
  /**
   * Create a plan to stream items from a producer to an iteratee by translating
   * the items using the given iteratee.
   * 
   * @param <I1> the enumerator's output type
   * @param <I2> the translator's output type
   * @param <O> the iteratee's output type
   * @param enumerator 
   * @param translator
   * @param iteratee
   * @return a plan that can be run to obtain results
   */
  <I1,I2,O> Plan3<I1,I2,O> createPlan(Enumerator<I1> enumerator, Iteratee<I1,I2> translator, Iteratee<I2,O> iteratee);
  
  /**
   * Compose two iteratees.
   * 
   * @param <A> the input type of the first iteratee
   * @param <B> the output type of the first iteratee and input type of the 
   * second
   * @param <C> the output type of the second iteratee 
   * @param first first iteratee
   * @param second second iteratee
   * @return a iteratee composed of the two iteratees
   */
  <A,B,C> Iteratee<A,C> compose(Iteratee<A,B> first, Iteratee<B,C> second);
  
  /**
   * Compose three iteratees.
   * 
   * @param <A> the input type of the first iteratee
   * @param <B> the output type of the first iteratee and input type of the 
   * second
   * @param <C> the output type of the second iteratee and input type of the 
   * third
   * @param <D> the output type of the third iteratee 
   * @param first first iteratee
   * @param second second iteratee
   * @param third third iteratee
   * @return a iteratee composed of the three iteratees
   */
  <A,B,C,D> Iteratee<A,D> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third
  );
  
  /**
   * Compose four iteratees.
   * 
   * @param <A> the input type of the first iteratee
   * @param <B> the output type of the first iteratee and input type of the 
   * second
   * @param <C> the output type of the second iteratee and input type of the 
   * third
   * @param <D> the output type of the third iteratee and input type of the 
   * fourth
   * @param <E> the output type of the fourth iteratee 
   * @param first first iteratee
   * @param second second iteratee
   * @param third third iteratee
   * @param fourth fourth iteratee
   * @return a iteratee composed of the four iteratees
   */
  <A,B,C,D,E> Iteratee<A,E> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third,
    Iteratee<D,E> fourth
  );
  /**
   * Compose five iteratees.
   * 
   * @param <A> the input type of the first iteratee
   * @param <B> the output type of the first iteratee and input type of the 
   * second
   * @param <C> the output type of the second iteratee and input type of the 
   * third
   * @param <D> the output type of the third iteratee and input type of the 
   * fourth
   * @param <D> the output type of the fourth iteratee and input type of the 
   * fifth
   * @param <E> the output type of the fifth iteratee 
   * @param first first iteratee
   * @param second second iteratee
   * @param third third iteratee
   * @param fourth fourth iteratee
   * @param fifth fifth iteratee
   * @return a iteratee composed of the five iteratees
   */
  <A,B,C,D,E,F> Iteratee<A,F> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third,
    Iteratee<D,E> fourth,
    Iteratee<E,F> fifth
  );  
}

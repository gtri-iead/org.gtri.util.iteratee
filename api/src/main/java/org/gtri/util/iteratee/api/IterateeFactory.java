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
 * An interface for a factory for creating implementations of various 
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
   * Create an Enumerator from a Enumerator.State.
   * 
   * @param <A> the output type
   * @param state state of some Enumerator
   * @return an Enumerator whose initialState method will return the provided
   * state
   */
  <A> Enumerator<A> createEnumerator(Enumerator.State<A> state);
  
  /**
   * Create an Iteratee from an Iteratee.State.
   * @param <I> the input type
   * @param <O> the output type
   * @param state state of some Iteratee
   * @return an Iteratee whose initialState method will return the provided
   * state
   */
  <I,O> Iteratee<I,O> createIteratee(Iteratee.State<I,O> state);
  
  /**
   * Create a plan to stream items from an Enumerator to an Iteratee.
   * 
   * @param <I> the enumerator's output type
   * @param <O> the iteratee's output type
   * @param enumerator
   * @param iteratee
   * @return a plan that can be run to stream items from the Enumerator to the 
   * Iteratee and obtain results
   */
  <I,O> Plan2<I,O> createPlan(Enumerator<I> enumerator, Iteratee<I,O> iteratee);
  
  /**
   * Create a plan to stream items from an Enumerator to an Iteratee of a 
   * different type by translating the items using the given Iteratee.
   * 
   * @param <I1> the enumerator's output type
   * @param <I2> the translator's output type
   * @param <O> the iteratee's output type
   * @param enumerator 
   * @param translator
   * @param iteratee
   * @return a plan that can be run to stream items from the Enumerator through 
   * the translating Iteratee to the terminal Iteratee and obtain results
   */
  <I1,I2,O> Plan3<I1,I2,O> createPlan(Enumerator<I1> enumerator, Iteratee<I1,I2> translator, Iteratee<I2,O> iteratee);
  
  /**
   * Compose two Iteratees.
   * 
   * @param <A> the input type of the first Iteratee
   * @param <B> the output type of the first Iteratee and input type of the 
   * second
   * @param <C> the output type of the second Iteratee 
   * @param first first Iteratee
   * @param second second Iteratee
   * @return a Iteratee composed of the two Iteratees
   */
  <A,B,C> Iteratee<A,C> compose(Iteratee<A,B> first, Iteratee<B,C> second);
  
  /**
   * Compose three Iteratees.
   * 
   * @param <A> the input type of the first Iteratee
   * @param <B> the output type of the first Iteratee and input type of the 
   * second
   * @param <C> the output type of the second Iteratee and input type of the 
   * third
   * @param <D> the output type of the third Iteratee 
   * @param first first Iteratee
   * @param second second Iteratee
   * @param third third Iteratee
   * @return a Iteratee composed of the three Iteratees
   */
  <A,B,C,D> Iteratee<A,D> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third
  );
  
  /**
   * Compose four Iteratees.
   * 
   * @param <A> the input type of the first Iteratee
   * @param <B> the output type of the first Iteratee and input type of the 
   * second
   * @param <C> the output type of the second Iteratee and input type of the 
   * third
   * @param <D> the output type of the third Iteratee and input type of the 
   * fourth
   * @param <E> the output type of the fourth Iteratee 
   * @param first first Iteratee
   * @param second second Iteratee
   * @param third third Iteratee
   * @param fourth fourth Iteratee
   * @return a Iteratee composed of the four Iteratees
   */
  <A,B,C,D,E> Iteratee<A,E> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third,
    Iteratee<D,E> fourth
  );
  /**
   * Compose five Iteratees.
   * 
   * @param <A> the input type of the first Iteratee
   * @param <B> the output type of the first Iteratee and input type of the 
   * second
   * @param <C> the output type of the second Iteratee and input type of the 
   * third
   * @param <D> the output type of the third Iteratee and input type of the 
   * fourth
   * @param <D> the output type of the fourth Iteratee and input type of the 
   * fifth
   * @param <E> the output type of the fifth Iteratee 
   * @param first first Iteratee
   * @param second second Iteratee
   * @param third third Iteratee
   * @param fourth fourth Iteratee
   * @param fifth fifth Iteratee
   * @return a Iteratee composed of the five Iteratees
   */
  <A,B,C,D,E,F> Iteratee<A,F> compose(
    Iteratee<A,B> first, 
    Iteratee<B,C> second,
    Iteratee<C,D> third,
    Iteratee<D,E> fourth,
    Iteratee<E,F> fifth
  );  
}

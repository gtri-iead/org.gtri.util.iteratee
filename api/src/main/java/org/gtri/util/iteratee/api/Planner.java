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
 * An interface that it used to perform planning operations on Producer, 
 * Consumer and Translator objects.
 * 
 * @author Lance
 */
public interface Planner {
  
  IterateeFactory factory();
  
  /**
   * Create a plan to connect a producer and a consumer.
   * 
   * @param <A> the input/output type
   * @param producer 
   * @param consumer
   * @return a plan that can be run to obtain results
   */
  <A,S> Consumer.Plan<A,A,S> connect(Producer<A> producer, Consumer<A,S> consumer);
  
  /**
   * Create a plan to connect a producer, a translator and a consumer.
   * 
   * @param <A> the input/output type
   * @param producer 
   * @param consumer
   * @return a plan that can be run to obtain results
   */
  <A,B,S> Consumer.Plan<A,B,S> connect(Producer<A> producer, Translator<A,B> translator, Consumer<B,S> consumer);
  
  /**
   * Create a plan to connect a producer and a builder.
   * 
   * @param <A> the input/output type
   * @param <A> the value type 
   * @param producer 
   * @param builder
   * @return a plan that can be run to obtain results
   */
  <A,V> Builder.Plan<A,A,V> connect(Producer<A> producer, Builder<A,V> builder);

  /**
   * Create a plan to connect a producer, a translator and a builder.
   * 
   * @param <A> the input/output type
   * @param <A> the value type 
   * @param producer 
   * @param builder
   * @return a plan that can be run to obtain results
   */
  <A,B,V> Builder.Plan<A,B,V> connect(Producer<A> producer, Translator<A,B> translator, Builder<B,V> builder);
  
  /**
   * Compose two translators.
   * 
   * @param <A> the input type of the first translator
   * @param <B> the output type of the first translator and input type of the 
   * second
   * @param <C> the output type of the second translator 
   * @param first first translator
   * @param second second translator
   * @return a translator composed of the two translators
   */
  <A,B,C> Translator<A,C> compose(Translator<A,B> first, Translator<B,C> second);
  
  /**
   * Compose three translators.
   * 
   * @param <A> the input type of the first translator
   * @param <B> the output type of the first translator and input type of the 
   * second
   * @param <C> the output type of the second translator and input type of the 
   * third
   * @param <D> the output type of the third translator 
   * @param first first translator
   * @param second second translator
   * @param third third translator
   * @return a translator composed of the three translators
   */
  <A,B,C,D> Translator<A,D> compose(
    Translator<A,B> first, 
    Translator<B,C> second,
    Translator<C,D> third
  );
  
  /**
   * Compose four translators.
   * 
   * @param <A> the input type of the first translator
   * @param <B> the output type of the first translator and input type of the 
   * second
   * @param <C> the output type of the second translator and input type of the 
   * third
   * @param <D> the output type of the third translator and input type of the 
   * fourth
   * @param <E> the output type of the fourth translator 
   * @param first first translator
   * @param second second translator
   * @param third third translator
   * @param fourth fourth translator
   * @return a translator composed of the four translators
   */
  <A,B,C,D,E> Translator<A,E> compose(
    Translator<A,B> first, 
    Translator<B,C> second,
    Translator<C,D> third,
    Translator<D,E> fourth
  );
  
  /**
   * Compose five translators.
   * 
   * @param <A> the input type of the first translator
   * @param <B> the output type of the first translator and input type of the 
   * second
   * @param <C> the output type of the second translator and input type of the 
   * third
   * @param <D> the output type of the third translator and input type of the 
   * fourth
   * @param <D> the output type of the fourth translator and input type of the 
   * fifth
   * @param <E> the output type of the fifth translator 
   * @param first first translator
   * @param second second translator
   * @param third third translator
   * @param fourth fourth translator
   * @param fifth fifth translator
   * @return a translator composed of the five translators
   */
  <A,B,C,D,E,F> Translator<A,F> compose(
    Translator<A,B> first, 
    Translator<B,C> second,
    Translator<C,D> third,
    Translator<D,E> fourth,
    Translator<E,F> fifth
  );
  
}

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
  /**
   * Concatenate the output of two producers. The first producer is run until
   * done then the second producer is run until done.
   * 
   * @param <A> the output type
   * @param lhs first producer
   * @param rhs second producer
   * @return a producer composed of the first followed by the second
   */
  <A> Producer<A> concat(Producer<A> lhs, Producer<A> rhs);
  
  /**
   * Concatenate the input of two consumers. The first consumer is fed input
   * until it is done then the second consumer is fed input until it is done.
   * 
   * @param <A> the input type
   * @param lhs first consumer
   * @param rhs second consumer
   * @return a consumer composed of the first followed by the second
   */
  <A> Consumer<A> concat(Consumer<A> lhs, Consumer<A> rhs);
  
  /**
   * Concatenate the input of a consumer and a builder. The consumer is fed 
   * input until it is done then the builder is fed input until it is done.
   * 
   * 
   * @param <A> the input type
   * @param <V> the type being built
   * @param lhs the consumer
   * @param rhs the builder
   * @return a builder composed of the consumer followed by the builder
   */
  <A,V> Builder<A,V> concat(Consumer<A> lhs, Builder<A,V> rhs);
  
  /**
   * Compose a consumer with a translator so that it may accept input of another
   * type. Input fed to the returned consumer is translated by the translator
   * and then fed to the original consumer.
   * 
   * @param <A> the type being translated from
   * @param <B> the type being translated to
   * @param translator 
   * @param consumer
   * @return a consumer that feeds its input to a translator which feeds its
   * translated output to the original consumer
   */
  <A,B> Consumer<A> translate(Translator<A,B> translator, Consumer<B> consumer);
  
  /**
   * Compose a builder with a translator so that it may accept input of another
   * type. Input fed to the returned builder is translated by the translator
   * and then fed to the original builder.
   * 
   * @param <A> the type being translated from
   * @param <B> the type being translated to
   * @param translator 
   * @param builder
   * @return a builder that feeds its input to a translator which feeds its
   * translated output to the original builder
   */
  <A,B,V> Builder<A,V> translate(Translator<A,B> translator, Builder<B,V> consumer);
  
  /**
   * Create a plan to connect a producer and a consumer.
   * 
   * @param <A> the input/output type
   * @param producer 
   * @param consumer
   * @return a plan that can be run to obtain results
   */
  <A> Consumer.Plan<A> connect(Producer<A> producer, Consumer<A> consumer);
  
  /**
   * Create a plan to connect a producer and a builder.
   * 
   * @param <A> the input/output type
   * @param producer 
   * @param builder
   * @return a plan that can be run to obtain results
   */  
  <A,V> Builder.Plan<A,V> connect(Producer<A> producer, Builder<A,V> builder);
}

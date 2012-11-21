/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.Function1;

/**
 *
 * @author Lance
 */
public interface IterateeFactory {
  ErrorHandlingCode errorHandlingCode();
  
  Planner createPlanner();
  <A,S> Producer<A> createProducer(Enumeratee<A,S> enumeratee);
  <A,S> Consumer<A,S> createConsumer(Iteratee<A,S> iteratee);
  <A,V> Builder<A,V> createBuilder(Iteratee<A,Builder.State<V>> iteratee);
  <A,B> Translator<A,B> createTranslator(Function1<A,B> f);
}

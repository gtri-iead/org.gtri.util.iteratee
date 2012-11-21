/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee;

import org.gtri.util.iteratee.api.Builder;
import org.gtri.util.iteratee.api.Builder.State;
import org.gtri.util.iteratee.api.Consumer;
import org.gtri.util.iteratee.api.Enumeratee;
import org.gtri.util.iteratee.api.ErrorHandlingCode;
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
  
  private final ErrorHandlingCode errorHandlingCode;
  
  public IterateeFactory() {
    this(ErrorHandlingCode.NORMAL);
  }
  public IterateeFactory(final ErrorHandlingCode errorHandlingCode) {
    this.errorHandlingCode = errorHandlingCode;
  }
  
  @Override
  public ErrorHandlingCode errorHandlingCode() {
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

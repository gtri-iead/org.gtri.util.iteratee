/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 * An interface for the immutable state of an Iteratee
 * 
 * @author lance.gatlin@gmail.com
 * @param <A> the input type
 * @param <S> the loop state type
 */
public interface IterateeState<A,S> extends MachineState<A,S,IterateeState<A,S>> {
  S loopState();
}

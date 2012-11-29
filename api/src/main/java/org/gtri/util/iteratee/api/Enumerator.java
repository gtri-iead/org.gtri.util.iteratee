/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 * 
 * @author lance.gatlin@gmail.com
 * @param <O> the output type
 * @param <E> the enumerator state type
 */
public interface Enumerator<O,E> {
  EnumeratorState<O,E> initialState();
}

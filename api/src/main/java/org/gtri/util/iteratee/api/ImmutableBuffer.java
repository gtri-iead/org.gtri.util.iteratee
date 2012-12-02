/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public interface ImmutableBuffer<A> extends Iterable<A> {
  static interface F<A> {
    void apply(A input);
  }
  
  void forEach(F f);
  
  int length();
  
  A apply(int idx);
}

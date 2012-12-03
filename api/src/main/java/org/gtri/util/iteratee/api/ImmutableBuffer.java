/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import java.util.Iterator;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public interface ImmutableBuffer<A> extends Iterable<A> {
  int length();
  
  A apply(int idx);
  
  // This is here because of an issue with Scala
  @Override
  Iterator<A> iterator();
}

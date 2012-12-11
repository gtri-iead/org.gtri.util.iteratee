/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class ImmutableBuffers {
  // Casting to any type is safe because the buffer will never hold any elements.
  @SuppressWarnings("unchecked")
  public static <A> ImmutableBuffer<A> empty() {
    return (ImmutableBuffer<A>)EMPTY;
  }
  private static final EmptyImmutableBuffer EMPTY = new EmptyImmutableBuffer();
  private static class EmptyImmutableBuffer implements ImmutableBuffer {

    @Override
    public int length() {
      return 0;
    }

    @Override
    public Object apply(int idx) {
      throw new NoSuchElementException();
    }

    @Override
    public Iterator iterator() {
      return new Iterator() {

        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public Object next() {
          throw new NoSuchElementException();
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException("Not supported.");
        }
      
      };
    }

    @Override
    public ImmutableBuffer append(ImmutableBuffer rhs) {
      return rhs;
    }

    @Override
    public ImmutableBuffer slice(int start, int end) {
      if(start == 0 && end == 0) {
        return this;
      } else {
        throw new NoSuchElementException();
      }
    }
    
    @Override
    public String toString() {
      return "ImmutableBuffer()";
    }
  }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.Option;

/**
 * An interface for the immutable state of a builder.
 * @author Lance
 */
public interface BuilderState<A,V> extends MachineState<A,Option<V>,BuilderState<A,V>> {
  Option<V> value();
}

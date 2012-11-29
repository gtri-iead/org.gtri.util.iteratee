/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 * An interface for a Mealy machine. See MachineState for details.
 * 
 * @author lance.gatlin@gmail.com
 * @param <I> the input type
 * @param <O> the output type
 * @param <M> the machine state type
 */
public interface Machine<I,O,M> {
  MachineState<I,O,M> initialState();
}

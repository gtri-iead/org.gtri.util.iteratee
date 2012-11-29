/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import scala.Unit;

/**
 *
 * @author Lance
 */
public interface ConsumerState<A> extends MachineState<A,Unit,ConsumerState<A>> {
}

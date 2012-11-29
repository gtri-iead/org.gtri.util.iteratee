/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 * An interface that represents the immutable state of a producer
 * 
 * @author Lance
 */
public interface ProducerState<A> extends EnumeratorState<A, ProducerState<A>> {
}

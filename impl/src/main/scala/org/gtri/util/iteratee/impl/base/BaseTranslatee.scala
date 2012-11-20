//package org.gtri.util.iteratee.impl.base
//
//import org.gtri.util.iteratee.api._
//import org.gtri.util.iteratee.api.Signals.EndOfInput
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 7:21 PM
// * To change this template use File | Settings | File Templates.
// */
//class BaseTranslatee[A,B,S](val downstream : Iteratee[B,S], val issues : List[Issue] = Nil) extends Translatee[A,B,S] {
//  def isDone = false
//
//  // TODO : how to inject issues here?
//  def state = downstream.state
//
//  def apply(eoi: EndOfInput) = this
//}

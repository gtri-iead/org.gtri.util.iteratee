//package org.gtri.util.iteratee.impl.concat
//
//import org.gtri.util.iteratee.api.{Iteratee, Producer}
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 11/18/12
//* Time: 4:59 PM
//* To change this template use File | Settings | File Templates.
//*/
//case class ConcatProducer[A](lhs : Producer[A], rhs : Producer[A]) extends Producer[A]{
//  def enumeratee(i: Iteratee[A]) = ConcatEnumeratee.concat(lhs.enumeratee(i), rhs.enumeratee(i))
//}

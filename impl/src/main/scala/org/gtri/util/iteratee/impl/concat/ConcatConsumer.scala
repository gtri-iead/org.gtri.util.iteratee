//package org.gtri.util.iteratee.impl.concat
//
//import org.gtri.util.iteratee.api.Consumer
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/18/12
// * Time: 4:52 PM
// * To change this template use File | Settings | File Templates.
// */
//case class ConcatConsumer[A](lhs : Consumer[A], rhs : Consumer[A]) extends Consumer[A] {
//  def iteratee() = ConcatIteratee.concat(lhs.iteratee, rhs.iteratee)
//}

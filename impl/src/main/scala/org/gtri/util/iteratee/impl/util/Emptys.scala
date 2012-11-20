//package org.gtri.util.iteratee.impl.util
//
//import org.gtri.util.iteratee.api.{Iteratee, StatusCode, Enumeratee}
//import org.gtri.util.iteratee.api.Signals.EndOfInput
//import Util.{BuilderIteratee, ConsumerIteratee}
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:09 AM
// * To change this template use File | Settings | File Templates.
// */
//case class EmptyProducer[A]() extends ReuseableProducer[A](EmptyEnumeratee())
//
//case class EmptyConsumer[A]() extends ReuseableConsumer[A](EmptyIteratee())
//
//case class EmptyBuilder[A,V]() extends ReuseableBuilder[A,V](EmptyIteratee())
//
//case class EmptyEnumeratee[A]() extends Enumeratee[A] {
//  def status() = StatusCode.SUCCESS
//
//  def downstream() = EmptyIteratee()
//
//  def downstream(p1: Iteratee[A]) = this
//
//  def step() = this
//
//  def run() = this
//}
//
//case class EmptyIteratee[A]() extends Iteratee[A] {
//  def status() = StatusCode.SUCCESS
//  def apply(item : A) = this
//  def apply(done : Done) = this
//}

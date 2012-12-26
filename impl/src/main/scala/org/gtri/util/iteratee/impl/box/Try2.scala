//package org.gtri.util.iteratee.impl.box
//
//import org.gtri.util.iteratee.box.Lazy
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 12/23/12
//* Time: 6:56 PM
//* To change this template use File | Settings | File Templates.
//*/
//object Try2 {
//  final case class Recover[+A](override val recoverable : Lazy[A]) extends Try2[A] {
//    override def isRecover : Boolean = true
//
//    def fold[X](ifRecover: Lazy[A] => X, ifGo: A => X) : X = ifRecover(recoverable)
//  }
//
//  object Recover {
//    def apply[A](recoverable: => A) : Recover[A] = Recover(Lazy(recoverable))
//  }
//
//  final case class Go[+A] (override val get : A) extends Try2[A] {
//    override def isGo : Boolean = true
//
//    def fold[X](ifRecover: Lazy[A] => X, ifGo: A => X) : X = ifGo(get)
//  }
//
////  def apply[A](a : A) : Try2[A] = Go(a)
////  def recover[A](recoverable: => Try2[A]) : Try2[A] = Recover(Lazy(recoverable))
//}
//
//sealed trait Try2[+A] {
//  import Try2._
//
//  // Override in inherited classes
//  def fold[X](ifRecover: Lazy[A] => X, ifGo: A => X) : X
//  def isRecover : Boolean = false
//  def isGo : Boolean = false
//  def get : A = throw new NoSuchElementException
//  def recoverable : Lazy[A] = throw new IllegalStateException
//
//  def recover : Try2[A] = fold(
//    ifRecover = { recoverable => Go(recoverable.value) },
//    ifGo = { a => this }
//  )
//
//  def flatMap[B](f: A => Try2[B]) : Try2[B] = {
//    fold(
//      ifRecover = {
//        recoverable =>
//            Recover({f(recoverable.value).recover.get})
//      },
//      ifGo = { a => f(a) }
//    )
//  }
//
//  def map[B](f: A => B) : Try2[B] = {
//    fold(
//      ifRecover = {
//        recoverable =>
//          Recover({ f(recoverable.value) })
//      },
//      ifGo = { a => Go(f(a)) }
//    )
//  }
//
////  /**
////   * Filter box
////   * @param p
////   * @return
////   */
////  def withFilter(p: A => Boolean): Try2[A] = {
////    fold(
////      ifRecover = {
////        recoverable =>
////      },
////      ifGo = {
////        a =>
////          if(p(a))
////            this
////          else
////            Try.nogo
////      }
////    )
////  }
//
//  /**
//   * Foreach for success box only. Recover or nogo box does not invoke f
//   * @param f
//   * @tparam U
//   */
//  def foreach[U](f: A => U) {
//    fold(
//      ifRecover = { _ => () },
//      ifGo = { a => f(a) }
//    )
//  }
//}
//

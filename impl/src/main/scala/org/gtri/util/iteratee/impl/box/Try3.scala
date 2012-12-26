//package org.gtri.util.iteratee.impl.box
//
//import org.gtri.util.iteratee.box.Lazy
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 12/16/12
//* Time: 10:01 PM
//* To change this template use File | Settings | File Templates.
//*/
//
//object Try3 {
//  final case class NoGo[+A,E](override val err : E) extends Try3[A,E] {
//    override def isNoGo : Boolean = true
//
//    def fold[X](ifNoGo: E => X, ifRecover: (E, Lazy[Try3[A,E]]) => X, ifGo: A => X) : X = ifNoGo(err)
//  }
//
//  final case class Recover[+A,E](override val err : E, override val recoverable : Lazy[Try3[A,E]]) extends Try3[A,E] {
//    override def isRecover : Boolean = true
//
//    def fold[X](ifNoGo: E => X, ifRecover: (E,Lazy[Try3[A,E]]) => X, ifGo: A => X) : X = ifRecover(err,recoverable)
//  }
//
//  object Recover {
//    def apply[A,E](err : E, recoverable: => Try3[A,E]) : Recover[A,E] = Recover(err, Lazy(recoverable))
//  }
//
//  final case class Go[+A,E] (override val get : A) extends Try3[A,E] {
//    override def isGo : Boolean = true
//
//    def fold[X](ifNoGo: E => X, ifRecover: (E,Lazy[Try3[A,E]]) => X, ifGo: A => X) : X = ifGo(get)
//  }
//
////  def nogo[A] : Try3[A] = NoGo
////  def apply[A](a : A) : Try3[A] = Go(a)
////  def recover[A](recoverable: => Try3[A]) : Try3[A] = Recover(Lazy(recoverable))
////  def fromOption[A](opt : Option[A]) : Try3[A] = {
////    if(opt.isDefined) {
////      Go(opt.get)
////    } else {
////      NoGo
////    }
////  }
//
//}
//
//sealed trait Try3[+A,M[+_]] {
//  import Try3._
//
//  // Override in inherited classes
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[M[A]] => X, ifGo: A => X) : X
//  def isNoGo : Boolean = false
//  def isRecover : Boolean = false
//  def isGo : Boolean = false
//  def get : A = throw new NoSuchElementException
//  def recoverable : Lazy[M[A]] = throw new IllegalStateException
//
//  def recover : Try3[A,M] = fold(
//    ifNoGo = { _ => this },
//    ifRecover = { (_, recoverable) => recoverable.value },
//    ifGo = { a => this }
//  )
//
//  /**
//   * Convert to an option. Go maps to Some, recover and nogo maps to None
//   * @return an option
//   */
//  def toOption : Option[A] = fold(
//    ifNoGo = { _ => None },
//    ifRecover = { (_,_) => None },
//    ifGo = { a => Some(a) }
//  )
//
//  /**
//   * Flatmap that short-circuits on nogo box. On recover box, returns a new Recover that can be recovered to proceed
//   * with the flatMap.
//   * @param f
//   * @tparam B
//   * @return
//   */
//  def flatMap[B](f: A => Try3[B,E]) : Try3[B,E] = {
//    fold(
//      ifNoGo = { NoGo(_) },
//      ifRecover = {
//        (err, recoverable) =>
//          lazy val tryB : Try3[B,E] = {
//            val outer = recoverable.value.recover
//            if(outer.isGo) {
//              f(outer.get).recover
//            } else {
//              NoGo(err)
//            }
//          }
//          Recover.apply[B,E](err,tryB)
//      },
//      ifGo = { a => f(a) }
//    )
//  }
//
//  /**
//   * Map that short-circuits on nogo box. On recover box, returns a new Recover that can be recovered to proceed
//   * with the map.
//   * @param f
//   * @tparam B
//   * @return
//   */
//  def map[B](f: A => B) : Try3[B,E] = {
//    fold(
//      ifNoGo = { NoGo(_) },
//      ifRecover = {
//        (err, recoverable) =>
//          lazy val tryB : Try3[B,E] = {
//            val outer = recoverable.value.recover
//            if(outer.isGo) {
//              Go(f(outer.get))
//            } else {
//              NoGo(err)
//            }
//          }
//          Recover.apply[B,E](err,tryB)
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
////  def withFilter(p: A => Boolean): Try3[A,E] = {
////    fold(
////      ifNoGo = { NoGo(_) },
////      ifRecover = {
////        (err, recoverable) =>
////          Recover(
////          err,
////          {
////            val outer = recoverable.value.recover
////            if(outer.isGo) {
////              f(outer.get).recover
////            } else {
////              NoGo(err)
////            }
////          }
////          )
////      },
////      ifGo = {
////        a =>
////          if(p(a))
////            this
////          else
////            NoGo
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
//      ifNoGo = { _ => () },
//      ifRecover = { (_,_) => () },
//      ifGo = { a => f(a) }
//    )
//  }
//}
//

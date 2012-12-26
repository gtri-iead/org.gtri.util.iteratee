package org.gtri.util.iteratee.impl.box

import language.higherKinds
import scalaz._
import Scalaz._

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 12/16/12
* Time: 10:01 PM
* To change this template use File | Settings | File Templates.
*/

object BoxM {
  type OptM[M[+_], +A] = M[Option[A]]
  type LazyOptM[M[+_], +A] = Lazy[OptM[M,A]]

  def empty[M[+_],A] : BoxM[M,A] = NoGo()
  def apply[M[+_],A](a : A) : BoxM[M,A] = Go(a)
  def fromOption[M[+_],A](opt : Option[A]) : BoxM[M,A] = {
    if(opt.isDefined) {
      Go(opt.get)
    } else {
      NoGo()
    }
  }
  def recover[M[+_],A](recoverable: => Lazy[OptM[M,A]]) : BoxM[M,A] = Recover(recoverable)
}

case class NoGo[M[+_],A]() extends BoxM[M,A] {
  import BoxM.LazyOptM

  override def isNoGo : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: LazyOptM[M,A] => X, ifGo: A => X) : X = ifNoGo
}

final case class Recover[M[+_], +A](override val recoverable : BoxM.LazyOptM[M,A]) extends BoxM[M, A] {
  import BoxM.LazyOptM

  override def isRecover : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: LazyOptM[M,A] => X, ifGo: A => X) : X = ifRecover(recoverable)
}

object Recover {
  import BoxM.OptM
  def apply[M[+_],A](recoverable: => OptM[M,A]) : Recover[M,A] = Recover(Lazy(recoverable))
}

final case class Go[M[+_], +A] (override val get : A) extends BoxM[M,A] {
  import BoxM.LazyOptM

  override def isGo : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: LazyOptM[M,A] => X, ifGo: A => X) : X = ifGo(get)
}


sealed trait BoxM[M[+_],+A] {
  import BoxM.OptM
  import BoxM.LazyOptM

  // Override in inherited classes
  def fold[X](ifNoGo: => X, ifRecover: LazyOptM[M,A] => X, ifGo: A => X) : X
  def isNoGo : Boolean = false
  def isRecover : Boolean = false
  def isGo : Boolean = false
  def get : A = throw new NoSuchElementException
  def recoverable : LazyOptM[M,A] = throw new IllegalStateException

  def recover(implicit OptM: Monad[M]) : OptM[M,A] = fold(
    ifNoGo = { OptM.pure(None) },
    ifRecover = { recoverable => recoverable.value },
    ifGo = { a => OptM.pure(Some(a)) }
  )

  def toOption : Option[A] = fold(
    ifNoGo = { None },
    ifRecover = { _ => None },
    ifGo = { a => Some(a) }
  )

  def flatMap[B](f: A => BoxM[M,B])(implicit OptM: Monad[M]) : BoxM[M,B] = fold(
      ifNoGo = { BoxM.empty },
      ifRecover = {
        recoverable =>
          Recover(Lazy({
            recoverable.value.flatMap { oa =>
              if(oa.isDefined) {
                f(oa.get).recover
              } else {
                OptM.pure(None)
              }
            }
          }))
      },
      ifGo = { a => f(a) }
    )

  def map[B](f: A => B)(implicit OptM: Monad[M]) : BoxM[M,B] = fold(
      ifNoGo = { BoxM.empty },
      ifRecover = {
        recoverable =>
          Recover(Lazy({
            recoverable.value.map { oa =>
              if(oa.isDefined) {
                Some(f(oa.get))
              } else {
                None
              }
            }
          }))
      },
      ifGo = { a => BoxM(f(a)) }
    )


  def foreach[U](f: A => U) {
    fold(
      ifNoGo = { () },
      ifRecover = { _ => () },
      ifGo = { a => f(a) }
    )
  }
}


//object Box {
//  type OptW[+W, +A] = Writer[W, Option[A]]
//  type LazyOptW[+W, +A] = Lazy[OptW[W,A]]
//
//  def empty[W,A] : Box[W,A] = NoGo
//  def apply[W,A](a : A) : Box[W,A] = Go(a)
//  def fromOption[W,A](opt : Option[A]) : Box[W,A] = {
//    if(opt.isDefined) {
//      Go(opt.get)
//    } else {
//      NoGo
//    }
//  }
//  def recover[W,A](recoverable: => Lazy[OptW[W,A]]) : Box[W,A] = Recover(recoverable)
//}
//
//case object NoGo extends Box[Nothing,Nothing] {
//  import Box.LazyOptW
//
//  override def isNoGo : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: LazyOptW[Nothing,Nothing] => X, ifGo: Nothing => X) : X = ifNoGo
//}
//
//final case class Recover[+W, +A](override val recoverable : Box.LazyOptW[W,A]) extends Box[W, A] {
//  import Box.LazyOptW
//
//  override def isRecover : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: LazyOptW[W,A] => X, ifGo: A => X) : X = ifRecover(recoverable)
//}
//
//object Recover {
//  import Box.OptW
//  def apply[W,A](recoverable: => OptW[W,A]) : Recover[W,A] = Recover(Lazy(recoverable))
//}
//
//final case class Go[+W, +A] (override val get : A) extends Box[W,A] {
//  import Box.LazyOptW
//
//  override def isGo : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: LazyOptW[W,A] => X, ifGo: A => X) : X = ifGo(get)
//}
//
//
//sealed trait Box[+W,+A] {
//  import Box.OptW
//  import Box.LazyOptW
//
//  // Override in inherited classes
//  def fold[X](ifNoGo: => X, ifRecover: LazyOptW[W,A] => X, ifGo: A => X) : X
//  def isNoGo : Boolean = false
//  def isRecover : Boolean = false
//  def isGo : Boolean = false
//  def get : A = throw new NoSuchElementException
//  def recoverable : LazyOptW[W,A] = throw new IllegalStateException
//
//  def recover[WW >: W](implicit M: Monoid[WW]) : OptW[WW,A] = fold(
//    ifNoGo = { Writer(M.zero,None) },
//    ifRecover = { recoverable => recoverable.value },
//    ifGo = { a => Writer(M.zero, Some(a)) }
//  )
//
//  /**
//   * Convert to an option. Go maps to Some, recover and nogo maps to None
//   * @return an option
//   */
//  def toOption : Option[A] = fold(
//    ifNoGo = { None },
//    ifRecover = { _ => None },
//    ifGo = { a => Some(a) }
//  )
//
//  def flatMap[WW >: W,B](f: A => Box[WW,B])(implicit M: Monoid[WW]) : Box[WW,B] = fold(
//    ifNoGo = { Box.empty },
//    ifRecover = {
//      recoverable =>
//        Recover(Lazy({
//          recoverable.value.flatMap { oa =>
//            if(oa.isDefined) {
//              f(oa.get).recover
//            } else {
//              Writer(M.zero, None)
//            }
//          }
//          //            val opt = recoverable.value.value
//          //            val log = recoverable.value.written
//          //            if(opt.isDefined) {
//          //              f(opt.get).recover :++> log
//          //            } else {
//          //              Writer(log, None)
//          //            }
//        }))
//    },
//    ifGo = { a => f(a) }
//  )
//
//  def map[WW >: W,B](f: A => B)(implicit M: Monoid[WW]) : Box[WW,B] = fold(
//    ifNoGo = { Box.empty },
//    ifRecover = {
//      recoverable =>
//        Recover(Lazy({
//          recoverable.value.flatMap { oa =>
//            if(oa.isDefined) {
//              Writer(M.zero, Some(f(oa.get)))
//            } else {
//              Writer(M.zero, None)
//            }
//          }
//          //            val opt = recoverable.value.value
//          //            val log = recoverable.value.written
//          //            if(opt.isDefined) {
//          //              Writer(log, Some(f(opt.get)))
//          //            } else {
//          //              Writer(log, None)
//          //            }
//        }))
//    },
//    ifGo = { a => Box(f(a)) }
//  )
//
//
//  def foreach[U](f: A => U) {
//    fold(
//      ifNoGo = { () },
//      ifRecover = { _ => () },
//      ifGo = { a => f(a) }
//    )
//  }
//}


//object Box {
//
//  def empty[A] : Box[A] = NoGo()
//  def apply[A](a : A) : Box[A] = Go(a)
//  def fromOption[A](opt : Option[A]) : Box[A] = {
//    if(opt.isDefined) {
//      Go(opt.get)
//    } else {
//      NoGo()
//    }
//  }
//  def recover[A](recoverable: => Lazy[OptLogWriter[A]]) : Box[A] = Recover(recoverable)
//}
//
//final case class NoGo[+A]() extends Box[A] {
//
//  override def isNoGo : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[OptLogWriter[A]] => X, ifGo: A => X) : X = ifNoGo
//}
//
//final case class Recover[+A](override val recoverable : Lazy[OptLogWriter[A]]) extends Box[A] {
//
//  override def isRecover : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[OptLogWriter[A]] => X, ifGo: A => X) : X = ifRecover(recoverable)
//}
//
//object Recover {
//  def apply[A](recoverable: => OptLogWriter[A]) : Recover[A] = Recover(Lazy(recoverable))
//}
//
//final case class Go[+A] (override val get : A) extends Box[A] {
//  override def isGo : Boolean = true
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[OptLogWriter[A]] => X, ifGo: A => X) : X = ifGo(get)
//}
//
//
//sealed trait Box[+A] {
//
//  // Override in inherited classes
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[OptLogWriter[A]] => X, ifGo: A => X) : X
//  def isNoGo : Boolean = false
//  def isRecover : Boolean = false
//  def isGo : Boolean = false
//  def get : A = throw new NoSuchElementException
//  def recoverable : Lazy[OptLogWriter[A]] = throw new IllegalStateException
//
//  def recover : OptLogWriter[A] = fold(
//    ifNoGo = { OptLogWriter.empty },
//    ifRecover = { recoverable => recoverable.value },
//    ifGo = { a => OptLogWriter(a) }
//  )
//
//  /**
//   * Convert to an option. Go maps to Some, recover and nogo maps to None
//   * @return an option
//   */
//  def toOption : Option[A] = fold(
//    ifNoGo = { None },
//    ifRecover = { _ => None },
//    ifGo = { a => Some(a) }
//  )
//
//  def flatMap[B](f: A => Box[B]) : Box[B] = fold(
//    ifNoGo = { Box.empty },
//    ifRecover = {
//      recoverable =>
//        Recover(Lazy({
//          val opt = recoverable.value.value
//          val log = recoverable.value.written
//          if(opt.isDefined) {
//            f(opt.get).recover :++> log
//          } else {
//            LogWriter(log, None)
//          }
//        }))
//    },
//    ifGo = { a => f(a) }
//  )
//
//  def map[B](f: A => B) : Box[B] = fold(
//    ifNoGo = { Box.empty },
//    ifRecover = {
//      recoverable =>
//        Recover(Lazy({
//          val opt = recoverable.value.value
//          val log = recoverable.value.written
//          if(opt.isDefined) {
//            LogWriter(log, Some(f(opt.get)))
//          } else {
//            LogWriter(log, None)
//          }
//        }))
//    },
//    ifGo = { a => Box(f(a)) }
//  )
//
//
//  def foreach[U](f: A => U) {
//    fold(
//      ifNoGo = { () },
//      ifRecover = { _ => () },
//      ifGo = { a => f(a) }
//    )
//  }
//}

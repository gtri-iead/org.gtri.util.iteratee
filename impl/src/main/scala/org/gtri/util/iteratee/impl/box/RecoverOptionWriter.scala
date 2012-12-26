//package org.gtri.util.iteratee.box
//
//import scalaz.Monoid
//
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 12/16/12
//* Time: 10:01 PM
//* To change this template use File | Settings | File Templates.
//*/
//
//object RecoverOptionWriter {
//
//  def fail[W,A](implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = fail[W,A](M.zero)
//  def fail[W,A](w : W)(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = new NoGo[W,A](w)
//
//  def apply[W,A](a : A)(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = apply[W,A](M.zero, a)
//  def apply[W,A](w : W, a : A)(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = new Go[W,A](w, a)
//
//  def recover[W,A](recoverable: => RecoverOptionWriter[W,A])(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = recover[W,A](M.zero,recoverable)
//  def recover[W,A](w : W, recoverable: => RecoverOptionWriter[W,A]) : RecoverOptionWriter[W,A] = new Recover[W,A](w, Lazy(recoverable))
//
//  def fromOption[W,A](opt : Option[A])(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = fromOption[W,A](M.zero, opt)
//  def fromOption[W,A](w : W, opt : Option[A]) = {
//    if(opt.isDefined) {
//      new Go[W,A](w, opt.get)
//    } else {
//      new NoGo[W,A](w)
//    }
//  }
//
//  def fromEither[W,A](either: Either[Lazy[RecoverOptionWriter[W,A]],Option[A]])(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = fromEither(M.zero,either)
//  def fromEither[W,A](w : W, either: Either[Lazy[RecoverOptionWriter[W,A]],Option[A]])(implicit M: Monoid[W]) : RecoverOptionWriter[W,A] = {
//    if(either.isLeft) {
//      new Recover[W,A](w, either.left.get)
//    } else {
//      val opt = either.right.get
//      if(opt.isDefined) {
//        new Go[W,A](w, opt.get)
//      } else {
//        new NoGo[W,A](w)
//      }
//    }
//  }
//}
//
//sealed trait RecoverOptionWriter[+W,+A] { self =>
//  import RecoverOptionWriter._
//
//  // Override in inherited classes
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[RecoverOptionWriter[W,A]] => X, ifGo: A => X) : X
//  def value : A = throw new NoSuchElementException
//  def written : W
//  def copy[WW >: W](w : WW) : RecoverOptionWriter[WW,A]
//  def isNoGo : Boolean = false
//  def isRecover : Boolean = false
//  def isGo : Boolean = false
//  def recoverable : Lazy[RecoverOptionWriter[W,A]] = throw new IllegalStateException
//
//
//  def :++>[WW >: W](w: WW)(implicit M: Monoid[WW]) = append(w)
//  def append[WW >: W](w: WW)(implicit M: Monoid[WW]) = copy(M.append(written, w))
//  def <++:[WW >: W](w: WW)(implicit M: Monoid[WW]) = prepend(w)
//  def prepend[WW >: W](w: WW)(implicit M: Monoid[WW]) = copy(M.append(w, written))
//  def reset[WW >: W](w: WW)(implicit M: Monoid[WW]) = copy(M.zero)
//
//  /**
//   * Recover from a failure, if needed. If box is not recover returns fail or success with no logs. If recover,
//   * returns recovered item and new logs
//   * @return a new Box with the result of the attempt
//   */
//  def recover[WW >: W](implicit M: Monoid[WW]) : RecoverOptionWriter[WW,A] = fold(
//    ifNoGo = { new NoGo(M.zero) },
//    ifRecover = { recoverable => recoverable.value },
//    ifGo = { a => new Go(M.zero, a) }
//  )
//
//  def recover2[WW >: W](implicit M: Monoid[WW]) : RecoverOptionWriter[WW,A] = fold(
//    ifNoGo = { this },
//    ifRecover = { recoverable => recoverable.value.asInstanceOf[RecoverOptionWriter[WW,A]].prepend(written) },
//    ifGo = { a => this }
//  )
//
//  /**
//   * Convert to an option. Go maps to Some, recover and fail maps to None
//   * @return an option
//  */
//  def toOption : Option[A] = fold(
//    ifNoGo = { None },
//    ifRecover = { _ => None },
//    ifGo = { a => Some(a) }
//  )
//
//  def toEither : Either[Lazy[RecoverOptionWriter[W,A]],Option[A]] = fold(
//    ifNoGo = { Right(None) },
//    ifRecover = { recoverable => Left(recoverable) },
//    ifGo = { a => Right(Some(a)) }
//  )
//
//  def >>[WW >: W, AA >: A](that : RecoverOptionWriter[WW,AA])(implicit M: Monoid[WW]) = fold(that)
//  def fold[WW >: W, AA >: A](that : RecoverOptionWriter[WW,AA])(implicit M: Monoid[WW]) : RecoverOptionWriter[WW,AA] = {
//    fold(
//      ifNoGo = that.fold(
//        ifNoGo = { that.append(this.written) },
//        ifRecover = { _ => that.append(this.written) },
//        ifGo = { _ => that.append(this.written) }
//      ),
//      ifRecover = {
//        _ => that.fold(
//          ifNoGo = { this.prepend(that.written) },
//          ifRecover = { _ => this.prepend(that.written) },
//          ifGo = { _ => that.append(this.written) }
//        )
//      },
//      ifGo = {
//        _ => that.fold(
//          ifNoGo = { this.prepend(that.written) },
//          ifRecover = { _ => this.prepend(that.written) },
//          ifGo = { _ => this.prepend(that.written) }
//        )
//      }
//    )
//  }
//
//  def flatMap[WW >: W, B](f: Option[A] => RecoverOptionWriter[WW,B])(implicit M: Monoid[WW]) : RecoverOptionWriter[WW,B] = {
//    fold(
//      ifNoGo = f(None).prepend(written),
//      ifRecover = {
//        recoverable =>
//          new Recover(
//            written,
//            Lazy({
//              val recoveredBox = recoverable.value
//              if(recoveredBox.isGo) {
//                f(Some(recoveredBox.value)).recover2.append(recoveredBox.written)
//              } else {
//                f(None).recover2.append(recoveredBox.written)
//              }
//            })
//          )
//      },
//      ifGo = { a => f(Some(a)).prepend(written) }
//    )
//  }
//
//  def map[WW >: W,B](f: Option[A] => Option[B])(implicit M: Monoid[WW]) : RecoverOptionWriter[WW,B] = {
//    fold(
//      ifNoGo = RecoverOptionWriter.fromOption(written, f(None)),
//      ifRecover = {
//        recoverable =>
//          new Recover(
//            written,
//            Lazy({
//              val recovered = recoverable.value
//              if(recovered.isGo) {
//                RecoverOptionWriter.fromOption(written, f(Some(recovered.value)))
//              } else {
//                RecoverOptionWriter.fromOption(written, f(None))
//              }
//            })
//          )
//      },
//      ifGo = { a => RecoverOptionWriter.fromOption(written, f(Some(a))) } // error here with { new GoBox(f(_),log) } ???
//    )
//  }
//
//  //  def flatMap[WW >: W, B](f: A => RecoverOptionWriter[WW,B])(implicit M: Monoid[WW]) : RecoverOptionWriter[WW,B] = {
////    fold(
////      ifNoGo = new NoGo(written),
////      ifRecover = {
////        recoverable =>
////          new Recover(
////            written,
////            Lazy({
////              val recoveredBox = recoverable.value
////              if(recoveredBox.isGo) {
////                f(recoveredBox.value).recover.prepend(recoveredBox.written)
////              } else {
////                new NoGo(recoveredBox.written)
////              }
////            })
////          )
////      },
////      ifGo = { a => f(a).prepend(written) }
////    )
////  }
////
////  def map[WW >: W,B](f: A => B)(implicit M: Monoid[WW]) : RecoverOptionWriter[WW,B] = {
////    fold(
////      ifNoGo = new NoGo(written),
////      ifRecover = {
////        recoverable =>
////          new Recover(
////            written,
////            Lazy({
////              val recovered = recoverable.value
////              if(recovered.isGo) {
////                new Go(recovered.written, f(recovered.value))
////              } else {
////                new NoGo(recovered.written)
////              }
////            })
////          )
////      },
////      ifGo = { a => new Go(written, f(a)) } // error here with { new GoBox(f(_),log) } ???
////    )
////  }
////
////  def withFilter(p: A => Boolean): RecoverOptionWriter[W,A] = {
////    fold(
////      ifNoGo = this,
////      ifRecover = {
////        recoverable =>
////          new Recover(
////            written,
////            Lazy({
////              val recovered = recoverable.value
////              if(recovered.isGo && p(recovered.value)) {
////                recovered
////              } else {
////                new NoGo(recovered.written)
////              }
////            })
////          )
////      },
////      ifGo = { a => if(p(a)) this else new NoGo(written) }
////    )
////  }
////
////  def foreach[U](f: A => U) {
////    fold(
////      ifNoGo = { () },
////      ifRecover = { _ => () },
////      ifGo = { a => f(a) }
////    )
////  }
//}
//
//final case class NoGo[+W, +A](written : W) extends RecoverOptionWriter[W,A] {
//  override def isNoGo : Boolean = true
//
//  def copy[WW >: W](w: WW) : RecoverOptionWriter[WW,A] = new NoGo[WW,A](w)
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[RecoverOptionWriter[W,A]] => X, ifGo: A => X) : X = ifNoGo
//}
//
//final case class Recover[+W, +A](written : W, override val recoverable : Lazy[RecoverOptionWriter[W,A]]) extends RecoverOptionWriter[W,A] {
//  override def isRecover : Boolean = true
//
//  def copy[WW >: W](w: WW) : RecoverOptionWriter[WW,A] = new Recover[WW,A](w, recoverable)
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[RecoverOptionWriter[W,A]] => X, ifGo: A => X) : X = ifRecover(recoverable)
//}
//
//final case class Go[+W, +A] (written : W, override val value : A) extends RecoverOptionWriter[W,A] {
//  override def isGo : Boolean = true
//
//  def copy[WW >: W](w: WW) : RecoverOptionWriter[WW,A] = new Go[WW,A](w, value)
//
//  def fold[X](ifNoGo: => X, ifRecover: Lazy[RecoverOptionWriter[W,A]] => X, ifGo: A => X) : X = ifGo(value)
//}
//

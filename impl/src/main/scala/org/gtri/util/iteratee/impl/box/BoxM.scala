/*
    Copyright 2012 Georgia Tech Research Institute

    Author: lance.gatlin@gtri.gatech.edu

    This file is part of org.gtri.util.iteratee library.

    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.

*/
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

  def empty[M[+_],A] : BoxM[M,A] = NoGo()
  def apply[M[+_],A](a : A) : BoxM[M,A] = Go(a)
  def fromOption[M[+_],A](opt : Option[A]) : BoxM[M,A] = {
    if(opt.isDefined) {
      Go(opt.get)
    } else {
      NoGo()
    }
  }
  def recover[M[+_],A](recoverable: => OptM[M,A]) : BoxM[M,A] = Recover(recoverable)
}

case class NoGo[M[+_],A]() extends BoxM[M,A] {
  override def isNoGo : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: Recover[M,A] => X, ifGo: A => X) : X = ifNoGo
}

final class Recover[M[+_], +A](__recoverable : => BoxM.OptM[M,A]) extends BoxM[M, A] {
  private lazy val _recoverable = __recoverable

  override def recoverable = _recoverable

  override def isRecover : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: Recover[M,A] => X, ifGo: A => X) : X = ifRecover(this)
}

object Recover {
  import BoxM.OptM
  def apply[M[+_],A](recoverable: => OptM[M,A]) : Recover[M,A] = new Recover[M,A](recoverable)
  def unapply[M[+_],A](box : BoxM[M,A]) : Option[OptM[M,A]] = {
    if(box.isRecover) {
      Some(box.recoverable)
    } else {
      None
    }
  }
}

final case class Go[M[+_], +A] (override val get : A) extends BoxM[M,A] {
  override def isGo : Boolean = true

  def fold[X](ifNoGo: => X, ifRecover: Recover[M,A] => X, ifGo: A => X) : X = ifGo(get)
}


sealed trait BoxM[M[+_],+A] {
  import BoxM.OptM

  // Override in inherited classes
  def fold[X](ifNoGo: => X, ifRecover: Recover[M,A] => X, ifGo: A => X) : X
  def isNoGo : Boolean = false
  def isRecover : Boolean = false
  def isGo : Boolean = false
  def get : A = throw new NoSuchElementException
  def recoverable : OptM[M,A] = throw new IllegalStateException

  def recover(implicit ev: Pointed[M]) : OptM[M,A] = fold(
    ifNoGo = { None.pure[M] },
    ifRecover = { recover => recover.recoverable },
    ifGo = { a => Some(a).pure[M] }
  )

  def toOption : Option[A] = fold(
    ifNoGo = { None },
    ifRecover = { _ => None },
    ifGo = { a => Some(a) }
  )

  def flatMap[B](f: A => BoxM[M,B])(implicit ev1: Monad[M]) : BoxM[M,B] = fold(
      ifNoGo = { BoxM.empty },
      ifRecover = {
        recover =>
          lazy val inner = {
            recover.recoverable.flatMap { oa =>
              if(oa.isDefined) {
                f(oa.get).recover
              } else {
                None.pure[M]
              }
            }
          }
          Recover(inner)
      },
      ifGo = { a => f(a) }
    )

  def map[B](f: A => B)(implicit ev: Functor[M]) : BoxM[M,B] = fold(
      ifNoGo = { BoxM.empty },
      ifRecover = {
        recover =>
          lazy val inner = {
            for(oa <- recover.recoverable) yield for(a <- oa) yield f(a)
          }
          Recover(inner)
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
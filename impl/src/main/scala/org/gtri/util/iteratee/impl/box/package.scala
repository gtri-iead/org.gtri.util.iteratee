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
package org.gtri.util.iteratee.impl

import scalaz._
import Scalaz._
import org.gtri.util.iteratee.api.Issue

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/24/12
 * Time: 7:35 AM
 * To change this template use File | Settings | File Templates.
 */
package object box {

  type LogWriter[+A] = Writer[List[Issue],A]
  object LogWriter {
    def apply[A](a : A) : LogWriter[A] = apply(Nil, a)
    def apply[A](issue : Issue, a : A) : LogWriter[A] = apply(List(issue), a)
    def apply[A](issues : List[Issue], a : A) : LogWriter[A] = Writer(issues, a)
  }

  type InnerBox[+A] = BoxM[LogWriter,A]
  type Box[+A] = LogWriter[InnerBox[A]]
  object Box {
    def empty[A] : Box[A] = empty[A](Nil)
    def empty[A](issue : Issue) : Box[A] = empty[A](List(issue))
    def empty[A](log : List[Issue]) : Box[A] = LogWriter(log, BoxM.empty[LogWriter,A])

    def apply[A](a : A) : Box[A] = apply[A](Nil,a)
    def apply[A](issue : Issue,a : A) : Box[A] = apply[A](List(issue),a)
    def apply[A](log : List[Issue],a : A) : Box[A] = LogWriter(log,BoxM[LogWriter,A](a))

    def recover[A](recoverable : => Box[A]) : Box[A] = recover[A](Nil,recoverable)
    def recover[A](issue : Issue,recoverable : => Box[A]) : Box[A] = recover[A](List(issue),recoverable)
    def recover[A](log : List[Issue],recoverable : => Box[A]) : Box[A] = {
      LogWriter(log, BoxM.recover[LogWriter,A](Lazy({
        val opt = recoverable.value.toOption
        val log = recoverable.written
        LogWriter(log,opt)
      })))
    }
  }

  implicit class boxAnything[A](self : A) {
    def box : Box[A] = Box(self)
    def box(issue : Issue) : Box[A] = box(List(issue))
    def box(log : List[Issue]) : Box[A] = Box(log, self)
  }

  def examples {

    import org.gtri.util.iteratee.api.Issue
    import org.gtri.util.iteratee.api.Issues._
    import scalaz._
    import Scalaz._

    val issue1 = log("asdf1",java.util.logging.Level.INFO)
    val issue2 = log("asdf2",java.util.logging.Level.INFO)
    val issue3 = log("asdf3",java.util.logging.Level.INFO)
    val issue4 = log("asdf4",java.util.logging.Level.INFO)
    val issue5 = log("asdf5",java.util.logging.Level.INFO)
    val issue6 = log("asdf6",java.util.logging.Level.INFO)
    val issue7 = log("asdf7",java.util.logging.Level.INFO)
    val issue8 = log("asdf8",java.util.logging.Level.INFO)

    val b1 : Box[Int] = 1.box(issue1)
    val b2 : Box[Int] = Box(issue2, 2)
    val b3 : Box[Int] = Box.empty(issue3)
    val b4 : Box[String] = Box(issue4, "asdf")
    // build a box that represents a failed operation that can be recovered
    val b5 : Box[String] = Box.recover(issue5, { println("here"); Box(issue7, "qwerty") })
    // build a box that represents a failed operation that will fail again when recovered
    val b6 : Box[String] = Box.recover(issue6, { println("here"); Box.empty(issue8) })

    b6.value.fold(
      ifGo = { a => println(a) },
      ifNoGo = { println("Nogo!") },
      ifRecover = {
        recoverable =>
          recoverable.value.value match {
            case Some(item) =>
              println(item)
            case None =>
              println("none1") // TODO: unreachable code here?
          }
      }
    )
    // TODO: unreachable code here?
//    b2.value match { // open a box to see if there is anything there
//      case Go(item) =>
//        println(item)
//      case Recover(recoverable) =>
//        recoverable.value.value match {
//          case Some(item) =>
//            println(item)
//          case None =>
//            println("none1")
//        }
//      case NoGo() =>
//        println("Nogo!")
//    }

    val b7 : Box[Int] =
      for(innerA <- b1;innerB <- b2) // get the InnerBox inside the LogWriter
      yield for(a <- innerA;b <- innerB) // get things out of InnerBox
      yield { println(a+b);a+b } // Do some stuff if everything works
    // b7 now contains LogWriter(List(issue1,issue2), Go(3))

    val b8 : Box[Int] =
      for(innerA <- b3;innerB <- b2) // get the InnerBox inside the LogWriter
      yield for(a <- innerA;b <- innerB) // get things out of InnerBox
      yield { println(a+b);a+b } // Do some stuff if everything works
    // b8 now contains LogWriter(List(issue3,issue2), NoGo())

    val b9 : Box[String] =
      for(innerA <- b5;innerB <- b2) // get the InnerBox inside the LogWriter
      yield for(a <- innerA;b <- innerB) // get things out of InnerBox
      yield { println(a+b);a+b } // Do some stuff if everything works
    // Since b5 is Recover, entire computation has been delayed until a recover command is issued
    // b9 now contains LogWriter(List(issue5,issue2), Recover(...))

    // Issue command to recover
    val b10 : LogWriter[Option[String]] = b9.value.recover
    // b10 now contains LogWriter(List(issue7),Some(qwerty2))

    val b11 = for(innerA <- b6;innerB <- b2) // get the InnerBox inside the LogWriter
              yield for(a <- innerA;b <- innerB) // get things out of InnerBox
              yield { println(a+b);a+b } // Do some stuff if everything works
    // Since b6 is Recover, entire computation has been delayed until a recover command is issued
    // b11 now contains LogWriter(List(issue6,issue2), Recover(...))

    // Issue command to recover
    val b12 : LogWriter[Option[String]] = b11.value.recover
    // b12 now contains LogWriter(List(issue8),None)
  }

}
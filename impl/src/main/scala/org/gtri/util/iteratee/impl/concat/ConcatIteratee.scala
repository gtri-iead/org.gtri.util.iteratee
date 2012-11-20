///*
//    Copyright 2012 Georgia Tech Research Institute
//
//    Author: lance.gatlin@gtri.gatech.edu
//
//    This file is part of org.gtri.util.iteratee library.
//
//    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.
//
//*/
//package org.gtri.util.iteratee.impl.concat
//
//import org.gtri.util.iteratee.api.Iteratee
//import org.gtri.util.iteratee.api.Signals.Done
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/18/12
// * Time: 1:07 AM
// * To change this template use File | Settings | File Templates.
// */
//case class ConcatIteratee[A](active : Iteratee[A], list : List[Iteratee[A]]) extends Iteratee[A] {
//  def status = active.status
//
//  def apply(item : A) = {
//    val nextIteratee = active(item)
//    if(nextIteratee.status.isDone && list.nonEmpty) {
//      val head :: tail = list
//      ConcatIteratee(head, tail)
//    } else {
//      ConcatIteratee(nextIteratee, list)
//    }
//  }
//
//  def apply(done : Done) = {
//    val nextIteratee = active(done)
//    if(nextIteratee.status.isDone && list.nonEmpty) {
//      val head :: tail = list
//      ConcatIteratee(head, tail)
//    } else {
//      ConcatIteratee(nextIteratee, list)
//    }
//  }
//}
//object ConcatIteratee {
//  def concat[A](lhs : Iteratee[A], rhs : Iteratee[A]) : Iteratee[A] = {
//    lhs match {
//      case ConcatIteratee(active, list) =>
//        rhs match {
//          case ConcatIteratee(innerActive, innerList) =>
//            ConcatIteratee(active, (innerActive :: innerList) ::: list)
//          case _ =>
//            ConcatIteratee(active, rhs :: list)
//        }
//      case _ =>
//        rhs match {
//          case ConcatIteratee(innerActive, innerList) =>
//            ConcatIteratee(lhs, (innerActive :: innerList))
//          case _ =>
//            ConcatIteratee(lhs, List(rhs))
//        }
//    }
//  }
//}

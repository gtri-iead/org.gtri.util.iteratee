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
//import org.gtri.util.iteratee.api
//import api.{Iteratee, Enumeratee}
//import org.gtri.util.iteratee.impl.util.Util
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 11/18/12
//* Time: 12:54 AM
//* To change this template use File | Settings | File Templates.
//*/
//case class ConcatEnumeratee[A](active : Enumeratee[A], list: List[Enumeratee[A]]) extends Enumeratee[A] {
//  def status = active.status
//
//  def downstream = active.downstream
//
//  def downstream(i: Iteratee[A]) = ConcatEnumeratee(active.downstream(i), list)
//
//  def step() = {
//    val nextEnumeratee = active.step()
//    if(nextEnumeratee.status.isDone && list.nonEmpty) {
//      val head :: tail = list
//      val nextActive = head.downstream(downstream)
//      ConcatEnumeratee(nextActive, tail)
//    } else {
//      ConcatEnumeratee(nextEnumeratee, list)
//    }
//  }
//
//  def run() = Util.run(this)
//}
//object ConcatEnumeratee {
//  def concat[A,S](lhs : Enumeratee[A,S], rhs : Enumeratee[A,S]) : Enumeratee[A] = {
//    lhs match {
//      case ConcatEnumeratee(active, list) =>
//        rhs match {
//          case ConcatEnumeratee(innerActive, innerList) =>
//            ConcatEnumeratee(active, (innerActive :: innerList) ::: list)
//          case _ =>
//            ConcatEnumeratee(active, rhs :: list)
//        }
//      case _ =>
//        rhs match {
//          case ConcatEnumeratee(innerActive, innerList) =>
//            ConcatEnumeratee(lhs, (innerActive :: innerList))
//          case _ =>
//            ConcatEnumeratee(lhs, List(rhs))
//        }
//    }
//  }
//}

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

///*
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
//package org.gtri.util.iteratee.impl
//
//import org.gtri.util.iteratee.api
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/15/12
// * Time: 2:02 PM
// * To change this template use File | Settings | File Templates.
// */
//case class Foldatee[A,U](val state : U, val fold: (U,A) => U) extends api.Foldatee[A,U] {
//
//  def apply(items : Traversable[A]) : Foldatee[A,U] = {
//    val nextState = items.foldLeft(state)(fold)
//    Foldatee(nextState, fold)
//  }
//  def apply(item : A) : Foldatee[A,U] = Foldatee(fold(state,item),fold)
//}
//
//object Foldatee {
////  def apply[A](list : List[A]) : Foldatee[List[A], A] = {
////    def fold(list : List[A],a : A) : List[A] = {
////      a :: list
////    }
////    new Foldatee(list, fold)
////  }
////
////  def apply[A,V](downstream : Iteratee[A,V]) : Foldatee[Iteratee[A,V], A] = {
////    def fold(i : Iteratee[A,V],a : A) : Iteratee[A,V] = pushTo(i,a)
////    new Foldatee(downstream, fold)
////  }
//}*/

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

//package org.gtri.util.iteratee.impl.translate
//
//import org.gtri.util.iteratee.api.{Iteratee, Translatee, Enumeratee}
//import org.gtri.util.iteratee.impl.base.BaseEnumeratee
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:21 AM
// * To change this template use File | Settings | File Templates.
// */
//class TranslatedEnumeratee[A,B,S](val e : EnumerateeWithATranslatee[A,B,S]) extends BaseEnumeratee[B,S](t.downstream) {
//  def isDone = e.isDone
//
//  def attach[T](i: Iteratee[B, T]) = {
//    val newT : Translatee[A,B,T] = e.downstream.attach(i)
//    val newE : EnumerateeWithATranslatee[A,B,T] = e.attach(newT)
//    new TranslatedEnumeratee[A,B,T](newE)
//  }
//
//  def step = {
//    val nextE = e.step
//    // TODO: find way to get rid of cast here
//    // For now, the downstream of e should always be a translatee - if not throw an error here anyways
//    new TranslatedEnumeratee(nextE)
//  }
//}

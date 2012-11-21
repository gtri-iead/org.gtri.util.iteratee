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

//package org.gtri.util.iteratee.impl
//
//import base.BaseTranslatee
//import org.gtri.util.iteratee.api
//import api.Builder.State
//import org.gtri.util.iteratee.api.{ErrorHandlingCode, Enumeratee, Iteratee, Translator, Producer, Consumer, Builder}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/21/12
// * Time: 5:58 AM
// * To change this template use File | Settings | File Templates.
// */
//class IterateeFactory(val errorHandlingCode : ErrorHandlingCode = ErrorHandlingCode.NORMAL) extends api.IterateeFactory {
//
//  def createPlanner() = new Planner(this)
//
//  def createProducer[A, S](e: Enumeratee[A, S]) = new Producer[A] {
//    def enumeratee[S](i: Iteratee[A, S]) = e.attach(i)
//  }
//
//  def createConsumer[A,S](i: Iteratee[A, S]) = new Consumer[A,S] {
//    def iteratee = i
//  }
//
//  def createBuilder[A, V](i: Iteratee[A, State[V]]) = new Builder[A,V] {
//    def iteratee = i
//  }
//
//  def createTranslator[A, B](f: (A) => B) = new Translator[A,B] {
//    def translatee[S](i: Iteratee[B, S]) = new TranslateeF(i, f)
//  }
//
//}

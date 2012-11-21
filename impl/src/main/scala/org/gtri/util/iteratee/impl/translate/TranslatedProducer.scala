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
//import org.gtri.util.iteratee.api._
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:25 AM
// * To change this template use File | Settings | File Templates.
// */
//case class TranslatedProducer[A,B,S](producerOfA : Producer[A], translator : Translator[A,B]) extends Producer[B] {
//
//  def enumeratee[S](i: Iteratee[B,S]) : Enumeratee[B,S] = {
//    val t = translator.translatee(i)
//    val e = producerOfA.enumeratee(t)
//
////    val t = translator.translatee(i)
////    val e = producer.enumeratee(t)
////    new TranslatedEnumeratee(e,t)
//  }
//}

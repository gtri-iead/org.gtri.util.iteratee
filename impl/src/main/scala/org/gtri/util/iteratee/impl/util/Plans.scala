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

//package org.gtri.util.iteratee.impl.util
//
//import org.gtri.util.iteratee.api
//import api.{Signals, Consumer, Producer, Builder}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:13 AM
// * To change this template use File | Settings | File Templates.
// */
//case class ConsumerPlan[A](producer : Producer[A], consumer : Consumer[A]) extends Consumer.Plan[A,A] {
//  }
//}
//case class BuilderPlan[A,V](producer : Producer[A], builder : Builder[A,V]) extends Builder.Plan[A,V] {
//  def run = {
//    val enumeratee = producer.enumeratee(builder.iteratee)
//    val lastE = enumeratee.run
//    val lastI = lastE.downstream
//    new Builder.Result[A,V] {
//      val eoiI = lastI(Signals.eoi)
//
//      def status = eoiI.state.status
//
//      def issues = eoiI.state.issues
//
//      def overflow = eoiI.state.overflow
//
//      def value = eoiI.state.value
//
//      def producer = ReuseableProducer(lastE)
//
//      def builder = ReuseableBuilder(lastI)
//    }
//  }
//}
//

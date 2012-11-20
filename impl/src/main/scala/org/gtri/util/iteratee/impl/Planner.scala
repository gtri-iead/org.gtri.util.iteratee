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

import org.gtri.util.iteratee.api
import api._
//import translate.{TranslatedProducer, TranslatedBuilder, TranslatedConsumer}
import translate.{TranslatedProducer}
import util.{BuilderPlan, ConsumerPlan}
//import concat.{ConcatConsumer, ConcatProducer}

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/11/12
* Time: 1:11 PM
* To change this template use File | Settings | File Templates.
*/
class Planner extends api.Planner {
//  def concat[A](lhs : Consumer[A], rhs : Consumer[A]) : Consumer[A] = ConcatConsumer(lhs, rhs)
//  def concat[A](lhs : Producer[A], rhs : Producer[A]) : Producer[A] = ConcatProducer(lhs, rhs)

  def connect[A](producer : Producer[A], consumer : Consumer[A]) : Consumer.Plan[A] = ConsumerPlan(producer, consumer)
  def connect[A,V](producer : Producer[A], builder : Builder[A,V]) : Builder.Plan[A,V] = BuilderPlan(producer, builder)

//  def translate[A,B](translator : Translator[A,B], consumer : Consumer[B]) : Consumer[A] = TranslatedConsumer(consumer, translator)
//  def translate[A,B,V](translator : Translator[A,B], builder : Builder[B,V]) : Builder[A,V] = TranslatedBuilder(builder, translator)
  def translate[A,B](translator : Translator[A,B], consumer : Consumer[B]) : Consumer[A] = null
  def translate[A,B,V](translator : Translator[A,B], builder : Builder[B,V]) : Builder[A,V] = null
  def translate[A,B](translator : Translator[A,B], producer : Producer[A]) : Producer[B] = TranslatedProducer(producer, translator)
}

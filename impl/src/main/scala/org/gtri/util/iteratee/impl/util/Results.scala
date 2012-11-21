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
//import org.gtri.util.iteratee.api.{Consumer, Issue, Producer, Builder}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:11 AM
// * To change this template use File | Settings | File Templates.
// */
//case class BuilderResult[A,V](
//  producer : Producer[A],
//  builder : Builder[A,V],
//  isSuccess : Boolean = false,
//  value : Option[V] = None,
//  issues : List[Issue] = Nil,
//  overflow : List[A] = Nil
//) extends Builder.Result[A,V]
//
//case class ConsumerResult[A](
//  producer : Producer[A] = EmptyProducer[A],
//  consumer : Consumer[A] = EmptyConsumer[A],
//  isSuccess : Boolean = false,
//  issues : List[Issue] = Nil,
//  overflow : List[A] = Nil
//) extends Consumer.Result[A]
//
//case class EmptyBuilderResult[A,V]() extends BuilderResult[A,V](EmptyProducer[A],EmptyBuilder[A,V])
//case class EmptyConsumerResult[A]() extends ConsumerResult[A](EmptyProducer[A],EmptyConsumer[A])

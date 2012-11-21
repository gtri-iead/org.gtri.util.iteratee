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
//import org.gtri.util.iteratee.api.{Iteratee, StatusCode, Enumeratee}
//import org.gtri.util.iteratee.api.Signals.EndOfInput
//import Util.{BuilderIteratee, ConsumerIteratee}
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:09 AM
// * To change this template use File | Settings | File Templates.
// */
//case class EmptyProducer[A]() extends ReuseableProducer[A](EmptyEnumeratee())
//
//case class EmptyConsumer[A]() extends ReuseableConsumer[A](EmptyIteratee())
//
//case class EmptyBuilder[A,V]() extends ReuseableBuilder[A,V](EmptyIteratee())
//
//case class EmptyEnumeratee[A]() extends Enumeratee[A] {
//  def status() = StatusCode.SUCCESS
//
//  def downstream() = EmptyIteratee()
//
//  def downstream(p1: Iteratee[A]) = this
//
//  def step() = this
//
//  def run() = this
//}
//
//case class EmptyIteratee[A]() extends Iteratee[A] {
//  def status() = StatusCode.SUCCESS
//  def apply(item : A) = this
//  def apply(done : Done) = this
//}

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
//import org.gtri.util.iteratee.api.{StatusCode, Iteratee, Status}
//import collection.immutable.Traversable
//import org.gtri.util.iteratee.impl.Iteratees._
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 12/1/12
// * Time: 10:04 PM
// * To change this template use File | Settings | File Templates.
// */
//class ParallelIterateeTuple2[I,O] (
//  iteratees : List[Iteratee[I,O]]
//  ) extends Iteratee[I,O] {
//  import ParallelIterateeTuple2._
//
//  def initialState = {
//    val iterateeStates = iteratees.map({ _.initialState })
//    State(iterateeStates.head, iterateeStates.tail)
//  }
//}
//
//object ParallelIterateeTuple2 {
//
//  case class State[I,O](
//    active : Iteratee.State[I,O],
//    remaining : List[Iteratee.State[I,O]],
//    done : List[Iteratee.State[I,O]] = Nil
//  ) extends Iteratee.State[I,O] {
//    val status = done.foldLeft(active.status) { (acc,i) => Status.sumAnd(acc, i.status)}
//
//    def apply(input: Traversable[I]) = {
//      val activeResult = active.apply(input)
//      val nextState : Iteratee.State[I,O] =
//        activeResult.next.status.statusCode match {
//          case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => State(activeResult.next, remaining, done)
//          case StatusCode.FATAL_ERROR => FatalError()
//          case StatusCode.SUCCESS =>
//            // TODO: apply EOI to active first
//            // TODO :feed overflow to next iteratee
//            // TODO: return result from overflow
//            State(remaining.head, remaining.tail, active :: done)
//        }
//      Result(nextState,activeResult.output, activeResult.overflow, activeResult.issues)
//    }
//
//    def endOfInput() = {
//      // TODO: just apply EOI to active
//    }
//
//  }
//
//}

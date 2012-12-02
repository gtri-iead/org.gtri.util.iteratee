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

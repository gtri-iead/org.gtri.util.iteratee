package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api.{Status, Iteratee, StatusCode}
import collection.immutable.Traversable
import org.gtri.util.iteratee.impl.Iteratees._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/30/12
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
case class IterateeStateTuple2[A,B,C](
  t1 : Iteratee.State[A,B],
  t2 : Iteratee.State[B,C],
  val status : Status
) extends Iteratee.State[A,C] {

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val issues = resultT1.issues ++ resultT2.issues
    val overflow = resultT1.overflow
    val output = resultT2.output
    val nextStatus = Status.sumAnd(resultT1.next.status, resultT2.next.status)
    val next : Iteratee.State[A,C] =
      nextStatus.statusCode match {
        case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => IterateeStateTuple2(resultT1.next, resultT2.next, nextStatus)
        case StatusCode.FATAL_ERROR => FatalError()
        case StatusCode.SUCCESS => Success()
      }
    Result(next, output, overflow, issues)
  }

  def endOfInput() = {
    val resultT1_EOI = t1.endOfInput()
    val resultT2 = t2.apply(resultT1_EOI.output)
    val resultT2_EOI = t2.endOfInput()
    val issues = resultT1_EOI.issues ++ resultT2.issues ++ resultT2_EOI.issues
    val overflow = resultT1_EOI.overflow
    val output = resultT2.output ++ resultT2_EOI.output
    val nextStatus = Status.sumAnd(resultT1_EOI.next.status, resultT2.next.status, resultT2_EOI.next.status)
    val next : Iteratee.State[A,C] =
      nextStatus.statusCode match {
        case StatusCode.CONTINUE | StatusCode.RECOVERABLE_ERROR => IterateeStateTuple2(resultT1_EOI.next, resultT2_EOI.next, nextStatus)
        case StatusCode.FATAL_ERROR => FatalError()
        case StatusCode.SUCCESS => Success()
      }
    Result(next, output, overflow, issues)
  }

}

case class IterateeTuple2[A,B,C](
  t1 : Iteratee[A,B],
  t2 : Iteratee[B,C]
) extends Iteratee[A,C] {
  def initialState = IterateeStateTuple2(t1.initialState, t2.initialState, Status.sumAnd(t1.initialState.status, t2.initialState.status))
}

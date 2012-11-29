package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import api._
import scala.collection.immutable.Traversable
import util.CollectionProducer

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
object Producers {
  val STD_CHUNK_SIZE = 256

  def apply[A](collection : Traversable[A]) = new CollectionProducer(collection)

  case class Result[A](next : Producer.State[A], output : Traversable[A], issues : Traversable[api.Issue] = Nil) extends Enumerator.State.Result[A,Producer.State[A]]

  abstract class Cont[A] extends Producer.State[A] {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A] extends Producer.State[A] {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  class Success[A] extends Producer.State[A] {
    def status = StatusCode.SUCCESS

    def step() = Result(this, Nil)
  }
  object Success {
    def apply[A]() = new Success[A]()

  }
  class FatalError[A] extends Producer.State[A] {
    def status = StatusCode.FATAL_ERROR

    def step() = Result(this, Nil)
  }
  object FatalError {
    def apply[A]() = new FatalError[A]()
  }
}

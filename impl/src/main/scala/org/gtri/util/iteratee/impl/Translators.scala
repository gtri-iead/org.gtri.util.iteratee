package org.gtri.util.iteratee.impl

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api._
import translate.TranslatorF


/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
object Translators {
  def apply[A,B](f: A => B) = new TranslatorF(f)

  case class Result[A,B](next : Translator.State[A,B], output : Traversable[B] = Nil, overflow : Traversable[A] = Nil, issues : Traversable[Issue] = Nil) extends Machine.State.Result[A,Traversable[B], Translator.State[A,B]]

  abstract class Cont[A,B] extends Translator.State[A,B] {
    def status = StatusCode.CONTINUE
  }

  abstract class RecoverableError[A,B] extends Translator.State[A,B] {
    def status = StatusCode.RECOVERABLE_ERROR
  }

  abstract class BaseDone[A,B] extends Translator.State[A,B] {
    def endOfInput = Result(this)
  }

  class Success[A,B] extends BaseDone[A,B] {
    def status = StatusCode.SUCCESS

    def apply(items: Traversable[A]) = Result(this, Nil, items)
  }
  object Success {
    def apply[A,B]() = new Success[A,B]()
  }
  class FatalError[A,B] extends BaseDone[A,B] {
    def status = StatusCode.FATAL_ERROR

    def apply(items: Traversable[A]) = Result(this, Nil, items)
  }
  object FatalError {
    def apply[A,B]() = new FatalError[A,B]()
  }

}

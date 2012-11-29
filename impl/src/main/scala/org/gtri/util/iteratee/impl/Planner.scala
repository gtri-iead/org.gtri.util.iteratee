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

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api
import api._
import translate._
import util.Looper


/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/11/12
* Time: 1:11 PM
* To change this template use File | Settings | File Templates.
*/
class Planner(val factory : api.Factory) extends api.Planner {
  val self = this
  def connect[A](p: Producer[A], c : Consumer[A]) =
    new Consumer.Plan[A,A] {

      def producer = p

      def consumer = c

      def run = {
        // TODO: find a way to do this without identity translatee
        val looper = new Looper[A,A,Unit,ProducerState[A],ConsumerState[A]](p.initialState, new IdentityTranslator[A]().initialState, c.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP = lastL.enumerator
        val lastC = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastC.endOfInput()

        new Consumer.Result[A,A] {

          def state = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues ++ looper.issues

          def overflow = eoiResult.overflow ++ looper.overflow

          def producer = factory.createProducer(lastP)

          def consumer = factory.createConsumer(lastC)
        }
      }
    }

  def connect[A, B](p: Producer[A], t: Translator[A, B], c: Consumer[B]) =
    new Consumer.Plan[A,B] {

      def producer = p

      def consumer = c

      def run = {
        val looper = new Looper[A,B,Unit,ProducerState[A],ConsumerState[B]](p.initialState, t.initialState, c.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP  = lastL.enumerator
        val lastC = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastC.endOfInput()

        new Consumer.Result[A,B] {

          def state = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastP)

          def consumer = factory.createConsumer(lastC)
        }
      }
    }

  def connect[A,S](p: Producer[A], i : Iteratee[A,S]) =
    new Iteratee.Plan[A,A,S] {

      def producer = p

      def iteratee = i

      def run = {
        // TODO: find a way to do this without identity translatee
        val looper = new Looper[A,A,S,ProducerState[A], IterateeState[A,S]](p.initialState, new IdentityTranslator[A]().initialState, i.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP = lastL.enumerator
        val lastI = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Iteratee.Result[A,A,S] {

          def loopState = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues ++ looper.issues

          def overflow = eoiResult.overflow ++ looper.overflow

          def producer = factory.createProducer(lastP)

          def iteratee = factory.createIteratee(lastI)
        }
      }
    }

  def connect[A, B, S](p: Producer[A], t: Translator[A, B], i: Iteratee[B,S]) =
    new Iteratee.Plan[A,B, S] {

      def producer = p

      def iteratee = i

      def run = {
        val looper = new Looper[A,B,S,ProducerState[A],IterateeState[B,S]](p.initialState, t.initialState, i.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP  = lastL.enumerator
        val lastI = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Iteratee.Result[A,B,S] {

          def loopState = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastP)

          def iteratee = factory.createIteratee(lastI)
        }
      }
    }

  def connect[A, V](p: Producer[A], b: Builder[A, V]) =
    new Builder.Plan[A,A,V] {

      def producer = p

      def builder = b

      def run = {
        // TODO: find a way to do this without identity translatee
        val looper = new Looper[A,A,Option[V],ProducerState[A], BuilderState[A,V]](p.initialState, new IdentityTranslator[A]().initialState, b.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP = lastL.enumerator
        val lastB = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastB.endOfInput()

        new Builder.Result[A,A,V] {
          def value = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastP)

          def builder = factory.createBuilder(lastB)
        }
      }
    }


  def connect[A, B, V](p: Producer[A], t: Translator[A, B], b: Builder[B, V]) =
    new Builder.Plan[A,B,V] {

      def producer = p

      def builder = b

      def run = {
        val looper = new Looper[A,B,Option[V],ProducerState[A],BuilderState[B,V]](p.initialState, t.initialState, b.initialState)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastP  = lastL.enumerator
        val lastB = lastL.machine
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastB.endOfInput()

        new Builder.Result[A,B,V] {

          def value = eoiResult.output

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastP)

          def builder = factory.createBuilder(lastB)
        }
      }
    }


  def compose[A, B, C](t1: Translator[A, B], t2: Translator[B, C]) = TranslatorTuple2(t1,t2)

  def compose[A, B, C, D](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D]) = TranslatorTuple3(t1,t2,t3)

  def compose[A, B, C, D, E](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E]) = TranslatorTuple4(t1,t2,t3,t4)

  def compose[A, B, C, D, E, F](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E], t5: Translator[E, F]) = TranslatorTuple5(t1,t2,t3,t4,t5)
}

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
import translate._
import util.Looper


/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/11/12
* Time: 1:11 PM
* To change this template use File | Settings | File Templates.
*/
class Planner(val factory : api.IterateeFactory) extends api.Planner {
  val self = this
  def connect[A,S](p: Producer[A], c : Consumer[A,S]) =
    new Consumer.Plan[A,A,S] {

      def producer = p

      def consumer = c

      def run = {
        // TODO: find a way to do this without identity translatee
        val looper = new Looper(p.enumeratee, new IdentityTranslatee[A], c.iteratee)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastE = lastL.enumeratee
        val lastI = lastL.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Consumer.Result[A,A,S] {

          def state = eoiResult.next.state

          def status = eoiResult.next.status

          def issues = eoiResult.issues ++ looper.issues

          def overflow = eoiResult.overflow ++ looper.overflow

          def producer = factory.createProducer(lastE)

          def consumer = factory.createConsumer(lastI)
        }
      }
    }

  def connect[A, B, S](p: Producer[A], t: Translator[A, B], c: Consumer[B,S]) =
    new Consumer.Plan[A,B, S] {

      def producer = p

      def consumer = c

      def run = {
        val looper = new Looper(p.enumeratee, t.translatee, c.iteratee)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastE  = lastL.enumeratee
        val lastI = lastL.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Consumer.Result[A,B,S] {

          def state = eoiResult.next.state

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastE)

          def consumer = factory.createConsumer(lastI)
        }
      }
    }


  def connect[A, V](p: Producer[A], b: Builder[A, V]) =
    new Builder.Plan[A,A,V] {

      def producer = p

      def builder = b

      def run = {
        // TODO: find a way to do this without identity translatee
        val looper = new Looper(p.enumeratee, new IdentityTranslatee[A], b.iteratee)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastE = lastL.enumeratee
        val lastI = lastL.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Builder.Result[A,A,V] {
          def value = eoiResult.next.state

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastE)

          def builder = factory.createBuilder(lastI)
        }
      }
    }


  def connect[A, B, V](p: Producer[A], t: Translator[A, B], b: Builder[B, V]) =
    new Builder.Plan[A,B,V] {

      def producer = p

      def builder = b

      def run = {
        val looper = new Looper(p.enumeratee, t.translatee, b.iteratee)
        val lastL = looper.run(factory.issueHandlingCode())
        val lastE  = lastL.enumeratee
        val lastI = lastL.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiResult = lastI.endOfInput()

        new Builder.Result[A,B,V] {

          def value = eoiResult.next.state

          def status = eoiResult.next.status

          def issues = eoiResult.issues

          def overflow = eoiResult.overflow

          def producer = factory.createProducer(lastE)

          def builder = factory.createBuilder(lastI)
        }
      }
    }


  def compose[A, B, C](t1: Translator[A, B], t2: Translator[B, C]) = TranslatorTuple2(t1,t2)

  def compose[A, B, C, D](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D]) = TranslatorTuple3(t1,t2,t3)

  def compose[A, B, C, D, E](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E]) = TranslatorTuple4(t1,t2,t3,t4)

  def compose[A, B, C, D, E, F](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E], t5: Translator[E, F]) = TranslatorTuple5(t1,t2,t3,t4,t5)
}

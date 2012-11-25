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
import api.Issue.ImpactCode
import translate._
import annotation.tailrec
import util.{PullIteratee, IterateeUtil}

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
        val e = p.enumeratee(c.iteratee)
        val lastE = IterateeUtil.run[A,S,Enumeratee[A,S]](factory.issueHandlingCode,e, { _.step() })
        val lastI = lastE.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI.endOfInput()

        new Consumer.Result[A,A,S] {

          def state = eoiI.state

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

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
        val innerE = p.enumeratee(PullIteratee())
        val translatedE = new TranslatedEnumeratee(innerE, t.translatee, Nil, c.iteratee())
        val lastE = IterateeUtil.run[B,S,TranslatedEnumeratee[A,B,S]](factory.issueHandlingCode,translatedE, { _.step() })
        val lastI = lastE.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI.endOfInput()

        new Consumer.Result[A,B,S] {

          def state() = eoiI.state

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

          def producer = factory.createProducer(lastE.innerEnumeratee)

          def consumer = factory.createConsumer(lastI)
        }
      }
    }


  def connect[A, V](p: Producer[A], b: Builder[A, V]) =
    new Builder.Plan[A,A,V] {

      def producer = p

      def builder = b

      def run = {
        val e = p.enumeratee(b.iteratee)
        val lastE = IterateeUtil.run[A,Option[V],Enumeratee[A,Option[V]]](factory.issueHandlingCode, e, { _.step() })
        val lastI = lastE.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI.endOfInput()

        new Builder.Result[A,A,V] {
          def value = eoiI.state

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

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
        val innerE = p.enumeratee(PullIteratee())
        val translatedE = new TranslatedEnumeratee(innerE, t.translatee, Nil, b.iteratee())
        val lastE = IterateeUtil.run[B,Option[V],TranslatedEnumeratee[A,B,Option[V]]](factory.issueHandlingCode, translatedE, { _.step() })
        val lastI = lastE.iteratee
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI.endOfInput()

        new Builder.Result[A,B,V] {

          def value = eoiI.state

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

          def producer = factory.createProducer(lastE.innerEnumeratee)

          def builder = factory.createBuilder(lastI)
        }
      }
    }


  def compose[A, B, C](t1: Translator[A, B], t2: Translator[B, C]) = TranslatorTuple2(t1,t2)

  def compose[A, B, C, D](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D]) = TranslatorTuple3(t1,t2,t3)

  def compose[A, B, C, D, E](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E]) = TranslatorTuple4(t1,t2,t3,t4)

  def compose[A, B, C, D, E, F](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E], t5: Translator[E, F]) = TranslatorTuple5(t1,t2,t3,t4,t5)
}

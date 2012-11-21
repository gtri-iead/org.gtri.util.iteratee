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
        val lastE = self.run(e)
        val lastI = lastE.downstream
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI(Signals.eoi)

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
        val e = p.enumeratee(t.translatee(c.iteratee))
        val lastE = self.run(e)
        val lastI = lastE.downstream
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI(Signals.eoi)

        new Consumer.Result[A,B,S] {

          def state() = eoiI.state

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

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
        val e = p.enumeratee(b.iteratee)
        val lastE = self.run(e)
        val lastI = lastE.downstream
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI(Signals.eoi)

        new Builder.Result[A,A,V] {
          def value = eoiI.state.value

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
        val e = p.enumeratee(t.translatee(b.iteratee))
        val lastE = self.run(e)
        val lastI = lastE.downstream
        // Feed EOI to lastI to force it to complete
        val eoiI = lastI(Signals.eoi)

        new Builder.Result[A,B,V] {

          def value = eoiI.state.value

          def status = eoiI.status

          def issues = eoiI.issues

          def overflow = eoiI.overflow

          def producer = factory.createProducer(lastE)

          def builder = factory.createBuilder(lastI)
        }
      }
    }


  def compose[A, B, C](t1: Translator[A, B], t2: Translator[B, C]) = TranslatorTuple2(t1,t2)

  def compose[A, B, C, D](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D]) = TranslatorTuple3(t1,t2,t3)

  def compose[A, B, C, D, E](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E]) = TranslatorTuple4(t1,t2,t3,t4)

  def compose[A, B, C, D, E, F](t1: Translator[A, B], t2: Translator[B, C], t3: Translator[C, D], t4: Translator[D, E], t5: Translator[E, F]) = TranslatorTuple5(t1,t2,t3,t4,t5)

  private def run[A,S](e: Enumeratee[A,S]) : Enumeratee[A,S] = {
    factory.issueHandlingCode match {
      case IssueHandlingCode.NORMAL => doNormalRun(e)
      case IssueHandlingCode.LAX => doLaxRun(e)
      case IssueHandlingCode.STRICT => doStrictRun(e)
    }
  }

  @tailrec
  private def doNormalRun[A,S](e: Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> e
        case StatusCode.CONTINUE => doNormalRun(e.step())
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }
  @tailrec
  private def doLaxRun[A,S](e: Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> doLaxRun(e.step())
        case StatusCode.CONTINUE => doLaxRun(e.step())
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }
  @tailrec
  private def doStrictRun[A,S](e: Enumeratee[A,S]) : Enumeratee[A,S] = {
    if(e.isDone) {
      e
    } else {
      e.status match {
        case StatusCode.RECOVERABLE_ERROR=> e
        case StatusCode.CONTINUE =>
          if(e.issues().filter({ _.impactCode() == ImpactCode.WARNING}).nonEmpty) {
            e
          } else {
            doStrictRun(e.step())
          }
        case StatusCode.SUCCESS => e
        case StatusCode.FATAL_ERROR => e
      }
    }
  }
}

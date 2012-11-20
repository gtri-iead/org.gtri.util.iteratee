package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api
import api.{Signals, Consumer, Producer, Builder}

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/19/12
 * Time: 2:13 AM
 * To change this template use File | Settings | File Templates.
 */
case class ConsumerPlan[A](producer : Producer[A], consumer : Consumer[A]) extends Consumer.Plan[A] {
  def run = {
    val enumeratee = producer.enumeratee(consumer.iteratee)
    val lastE = enumeratee.run
    val lastI = lastE.downstream
    val eoiI = lastI(Signals.eoi())
    new Consumer.Result[A] {

      def status() = eoiI.state().status()

      def issues() = eoiI.state().issues()

      def overflow() = eoiI.state().overflow()

      def producer() = ReuseableProducer(lastE)

      def consumer() = ReuseableConsumer(lastI)
    }
  }
}
case class BuilderPlan[A,V](producer : Producer[A], builder : Builder[A,V]) extends Builder.Plan[A,V] {
  def run = {
    val enumeratee = producer.enumeratee(builder.iteratee)
    val lastE = enumeratee.run
    val lastI = lastE.downstream
    val eoiI = lastI(Signals.eoi())
    new Builder.Result[A,V] {

      def status() = eoiI.state().status()

      def issues() = eoiI.state().issues()

      def overflow() = eoiI.state().overflow()

      def value() = eoiI.state().value()

      def producer() = ReuseableProducer(lastE)

      def builder() = ReuseableBuilder(lastI)
    }
  }
}

